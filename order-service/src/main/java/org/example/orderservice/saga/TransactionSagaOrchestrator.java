package org.example.orderservice.saga;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orderservice.entity.*;
import org.example.orderservice.enumeration.OrderStatusEnum;
import org.example.orderservice.exception.custom.AlreadyProcessedException;
import org.example.orderservice.kafka.KafkaSenderService;
import org.example.orderservice.kafka.command.ReleaseAmountCommand;
import org.example.orderservice.kafka.command.ReserveAmountCommand;
import org.example.orderservice.kafka.command.TransferMoneyCommand;
import org.example.orderservice.kafka.event.AmountReleasedEvent;
import org.example.orderservice.kafka.event.MoneyTransferredEvent;
import org.example.orderservice.repository.*;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.repository.OrderStatusRepository;
import org.example.orderservice.repository.TransactionRepository;
import org.example.orderservice.repository.TransactionSagaRepository;
import org.example.orderservice.service.CustomerPortfolioService;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderStatus;
import org.example.orderservice.entity.Transaction;
import org.example.orderservice.entity.TransactionSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionSagaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(TransactionSagaOrchestrator.class);

    private final KafkaSenderService kafkaSender;
    private final TransactionSagaRepository sagaRepository;
    private final CustomerPortfolioService portfolioService;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ObjectMapper objectMapper;

    private static final int SAGA_TIMEOUT_MINUTES = 1;

    public TransactionSagaOrchestrator(
            KafkaSenderService kafkaSender,
            TransactionSagaRepository sagaRepository,
            CustomerPortfolioService portfolioService,
            OrderRepository orderRepository,
            TransactionRepository transactionRepository,
            OrderStatusRepository orderStatusRepository,
            ObjectMapper objectMapper
    ) {
        this.kafkaSender = kafkaSender;
        this.sagaRepository = sagaRepository;
        this.portfolioService = portfolioService;
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.objectMapper = objectMapper;
    }

    public void start(TransactionSagaContext ctx) {
        log.info("Saga {} started: buyOrderId={}, sellOrderId={}, quantity={}",
                ctx.sagaId, ctx.buyOrderId, ctx.sellOrderId, ctx.quantity);
        saveSaga(ctx, SagaStep.START, "ACTIVE", null);
        kafkaSender.send(
                "money.transfer",
                ctx.sagaId,
                new TransferMoneyCommand(
                        ctx.sagaId,
                        ctx.moneyOpId,
                        ctx.buyerId,
                        ctx.sellerId,
                        ctx.currencyId,
                        ctx.transferAmount,
                        ctx.totalFee
                )
        );
    }

    @Scheduled(fixedDelay = 60_000)
    public void recoverStaleSagas() {
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(SAGA_TIMEOUT_MINUTES);
        List<TransactionSaga> stale = sagaRepository.findStaleActiveSagas(deadline);

        for (TransactionSaga saga : stale) {
            try {
                TransactionSagaContext ctx = saga.getContext();
                log.warn("Saga {} timed out, starting compensation", ctx.sagaId);
                saveSaga(ctx, SagaStep.valueOf(saga.getStep()), "TIMED_OUT", "Saga timed out");
                rollback(ctx);
            } catch (Exception e) {
                log.error("Failed to rollback timed-out saga {}", saga.getSagaId(), e);
            }
        }
    }

    @KafkaListener(topics = "money.transferred", groupId = "saga-group")
    public void onMoneyTransferred(byte[] message) throws Exception {
        MoneyTransferredEvent event = objectMapper.readValue(message, MoneyTransferredEvent.class);

        TransactionSaga saga = sagaRepository
                .findTopBySagaIdOrderByCreatedAtDesc(event.getSagaId())
                .orElse(null);
        if (saga == null || isTerminal(saga.getStatus())) {
            log.warn("Ignoring money.transferred for saga {} (status={})",
                    event.getSagaId(), saga != null ? saga.getStatus() : "null");
            return;
        }

        TransactionSagaContext ctx = saga.getContext();

        if (!event.isSuccess()) {
            log.error("money.transferred failed for saga {}: {}", ctx.sagaId, event.getErrorMessage());
            handleFailure(SagaStep.START, ctx, event.getErrorMessage());
            return;
        }

        log.info("Saga {} money.transferred succeeded, moving to amount.release", ctx.sagaId);
        saveSaga(ctx, SagaStep.MONEY_TRANSFERRED, "ACTIVE", null);

        kafkaSender.send(
                "amount.release",
                ctx.sagaId,
                new ReleaseAmountCommand(
                        ctx.sagaId,
                        ctx.releaseOpId,
                        ctx.buyerId,
                        ctx.currencyId,
                        ctx.releaseAmount
                )
        );
    }

    @KafkaListener(topics = "amount.released", groupId = "saga-group")
    public void onAmountReleased(byte[] message) throws Exception {
        AmountReleasedEvent event = objectMapper.readValue(message, AmountReleasedEvent.class);

        TransactionSaga saga = sagaRepository
                .findTopBySagaIdOrderByCreatedAtDesc(event.getSagaId())
                .orElse(null);
        if (saga == null || isTerminal(saga.getStatus())) {
            log.warn("Ignoring amount.released for saga {} (status={})",
                    event.getSagaId(), saga != null ? saga.getStatus() : "null");
            return;
        }

        TransactionSagaContext ctx = saga.getContext();

        if (!event.isSuccess()) {
            log.error("amount.released failed for saga {}: {}", ctx.sagaId, event.getErrorMessage());
            handleFailure(SagaStep.MONEY_TRANSFERRED, ctx, event.getErrorMessage());
            return;
        }

        log.info("Saga {} amount.released succeeded, moving to security transfer", ctx.sagaId);
        saveSaga(ctx, SagaStep.RESERVE_RELEASED, "ACTIVE", null);

        try {
            portfolioService.transferSecurity(
                    ctx.securityId,
                    ctx.buyerId,
                    ctx.sellerId,
                    ctx.quantity,
                    ctx.executedPrice,
                    ctx.feePerUnit,
                    ctx.securityOpId
            );
        } catch (AlreadyProcessedException e) {
            log.warn("Security transfer already processed for saga {}, continuing", ctx.sagaId);
        } catch (Exception e) {
            log.error("Security transfer failed for saga {}: {}", ctx.sagaId, e.getMessage());
            handleFailure(SagaStep.RESERVE_RELEASED, ctx, e.getMessage());
            return;
        }

        saveSaga(ctx, SagaStep.SECURITY_TRANSFERRED, "ACTIVE", null);
        finalizeSaga(ctx);
        saveSaga(ctx, SagaStep.COMPLETED, "COMPLETED", null);
        log.info("Saga {} completed successfully", ctx.sagaId);
    }

    private void handleFailure(SagaStep step, TransactionSagaContext ctx, String error) {
        log.warn("Saga {} failed at step {} with error: {}", ctx.sagaId, step, error);
        saveSaga(ctx, step, "FAILED", error);
        rollback(ctx);
    }

    private void rollback(TransactionSagaContext ctx) {
        TransactionSaga last = sagaRepository
                .findTopBySagaIdOrderByCreatedAtDesc(ctx.sagaId)
                .orElse(null);
        if (last != null && last.getStep().contains("ROLLBACK")) {
            log.warn("Saga {} already in rollback process, skipping", ctx.sagaId);
            return;
        }

        log.info("Starting rollback for saga {}", ctx.sagaId);
        saveSaga(ctx, SagaStep.ROLLBACK_STARTED, "ROLLING_BACK", null);

        try {
            portfolioService.transferSecurity(
                    ctx.securityId,
                    ctx.sellerId,
                    ctx.buyerId,
                    ctx.quantity,
                    ctx.executedPrice,
                    ctx.feePerUnit,
                    ctx.rollbackSecurityOpId
            );
        } catch (AlreadyProcessedException e) {
            log.warn("Rollback security transfer already processed for saga {}", ctx.sagaId);
        } catch (Exception e) {
            log.warn("Rollback security transfer failed (may be already done): {}", e.getMessage());
        }

        kafkaSender.send(
                "amount.reserve",
                ctx.sagaId,
                new ReserveAmountCommand(
                        ctx.sagaId,
                        ctx.rollbackReserveOpId,
                        ctx.buyerId,
                        ctx.currencyId,
                        ctx.releaseAmount
                )
        );

        kafkaSender.send(
                "money.transfer",
                ctx.sagaId,
                new TransferMoneyCommand(
                        ctx.sagaId,
                        ctx.rollbackMoneyOpId,
                        ctx.sellerId,
                        ctx.buyerId,
                        ctx.currencyId,
                        ctx.transferAmount,
                        ctx.totalFee
                )
        );

        Order buyOrder = orderRepository.findById(ctx.buyOrderId).orElse(null);
        Order sellOrder = orderRepository.findById(ctx.sellOrderId).orElse(null);
        if (buyOrder != null) {
            buyOrder.setAvailableQuantity(buyOrder.getAvailableQuantity() + ctx.quantity);
            orderRepository.save(buyOrder);
            log.info("Restored buy order {} availableQuantity to {}", buyOrder.getId(), buyOrder.getAvailableQuantity());
        }
        if (sellOrder != null) {
            sellOrder.setAvailableQuantity(sellOrder.getAvailableQuantity() + ctx.quantity);
            orderRepository.save(sellOrder);
            log.info("Restored sell order {} availableQuantity to {}", sellOrder.getId(), sellOrder.getAvailableQuantity());
        }

        saveSaga(ctx, SagaStep.ROLLBACK_COMPLETED, "FAILED", null);
        log.warn("Saga {} rollback completed", ctx.sagaId);
    }

    private void finalizeSaga(TransactionSagaContext ctx) {
        Order buyOrder = orderRepository.findById(ctx.buyOrderId).orElseThrow();
        Order sellOrder = orderRepository.findById(ctx.sellOrderId).orElseThrow();

        updateOrderStatus(buyOrder);
        updateOrderStatus(sellOrder);
        orderRepository.save(buyOrder);
        orderRepository.save(sellOrder);

        Transaction tx = new Transaction(
                buyOrder,
                sellOrder,
                ctx.quantity,
                ctx.executedPrice,
                ctx.totalFee,
                LocalDateTime.now()
        );

        try {
            transactionRepository.save(tx);
        } catch (DataIntegrityViolationException e) {
            log.warn("Transaction already exists for orders {}/{}, skipping", buyOrder.getId(), sellOrder.getId());
        }
    }

    private void saveSaga(TransactionSagaContext ctx, SagaStep step,
                          String status, String error) {
        try {
            TransactionSaga saga = new TransactionSaga(
                    ctx.sagaId,
                    step.name(),
                    status,
                    ctx,
                    error,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            sagaRepository.save(saga);
        } catch (Exception e) {
            log.error("Failed to save saga state for sagaId={}", ctx.sagaId, e);
            throw new RuntimeException(e);
        }
    }

    private void updateOrderStatus(Order order) {
        OrderStatus newStatus;
        if (order.getAvailableQuantity() == 0) {
            newStatus = orderStatusRepository.findByStatus(OrderStatusEnum.EXECUTED).orElseThrow();
        } else if (order.getAvailableQuantity().equals(order.getQuantity())) {
            newStatus = orderStatusRepository.findByStatus(OrderStatusEnum.OPEN).orElseThrow();
        } else {
            newStatus = orderStatusRepository.findByStatus(OrderStatusEnum.PARTIALLY_EXECUTED).orElseThrow();
        }
        order.setOrderStatus(newStatus);
    }

    private boolean isTerminal(String status) {
        return "COMPLETED".equals(status)
                || "FAILED".equals(status)
                || "TIMED_OUT".equals(status)
                || "ROLLING_BACK".equals(status);
    }
}