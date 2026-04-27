package org.example.orderserivce.service;

import org.example.orderserivce.saga.TransactionSagaContext;
import org.example.orderserivce.saga.TransactionSagaOrchestrator;
import org.springframework.transaction.annotation.Transactional;
import org.example.orderserivce.client.CurrencyClient;
import org.example.orderserivce.client.SavingsAccountClient;
import org.example.orderserivce.client.SecurityClient;
import org.example.orderserivce.dto.request.ReserveAmountRequestDto;
import org.example.orderserivce.dto.request.SecurityDto;
import org.example.orderserivce.dto.response.TransferResponseDto;
import org.example.orderserivce.entity.Order;
import org.example.orderserivce.entity.OrderStatus;
import org.example.orderserivce.entity.Transaction;
import org.example.orderserivce.enumeration.CurrencyCodeEnum;
import org.example.orderserivce.enumeration.OrderStatusEnum;
import org.example.orderserivce.enumeration.OrderTypeEnum;
import org.example.orderserivce.mapper.OrderMapper;
import org.example.orderserivce.mapper.TransactionBuyMapper;
import org.example.orderserivce.mapper.TransactionSellMapper;
import org.example.orderserivce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderMatchingService {

    private final SavingsAccountClient savingsAccountClient;
    private final SecurityClient securityClient;
    private final OrderRepository orderRepository;
    private final CustomerPortfolioRepository portfolioRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderTypeRepository orderTypeRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerPortfolioService customerPortfolioService;
    private final TransactionSagaOrchestrator sagaOrchestrator;

    public OrderMatchingService(SavingsAccountClient savingsAccountClient, SecurityClient securityClient, OrderRepository orderRepository, CustomerPortfolioRepository portfolioRepository, OrderStatusRepository orderStatusRepository, OrderTypeRepository orderTypeRepository, TransactionRepository transactionRepository, CustomerPortfolioService customerPortfolioService, TransactionSagaOrchestrator sagaOrchestrator) {
        this.savingsAccountClient = savingsAccountClient;
        this.securityClient = securityClient;
        this.orderRepository = orderRepository;
        this.portfolioRepository = portfolioRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderTypeRepository = orderTypeRepository;
        this.transactionRepository = transactionRepository;
        this.customerPortfolioService = customerPortfolioService;
        this.sagaOrchestrator = sagaOrchestrator;
    }

    public void matchOrder(Order newOrder) {
        boolean isBuy = newOrder.getOrderType().getType() == OrderTypeEnum.BUY;

        List<Order> oppositeOrders = isBuy
                ? orderRepository.findBestSellMatchForBuy(newOrder.getSecurityId(), newOrder.getPrice())
                : orderRepository.findBestBuyMatchForSell(newOrder.getSecurityId(), newOrder.getPrice());

        SecurityDto security = null;
        try {
            security = securityClient.getById(newOrder.getSecurityId());
        } catch (Exception e) {
            return;
        }

        for (Order opposite : oppositeOrders) {
            if (newOrder.getAvailableQuantity() == 0) {
                break;
            }
            if (opposite.getAvailableQuantity() == 0) {
                continue;
            }

            Order buyOrder = isBuy ? newOrder : opposite;
            Order sellOrder = isBuy ? opposite : newOrder;

            int executedQuantity = Math.min(
                    buyOrder.getAvailableQuantity(),
                    sellOrder.getAvailableQuantity()
            );

            if (executedQuantity == 0) {
                continue;
            }

            buyOrder.setAvailableQuantity(buyOrder.getAvailableQuantity() - executedQuantity);
            sellOrder.setAvailableQuantity(sellOrder.getAvailableQuantity() - executedQuantity);
            orderRepository.save(buyOrder);
            orderRepository.save(sellOrder);

            sagaOrchestrator.start(
                    buildContext(buyOrder, sellOrder, opposite.getPrice(), executedQuantity, security)
            );
        }
    }

    private TransactionSagaContext buildContext(
            Order buyOrder,
            Order sellOrder,
            BigDecimal executedPrice,
            Integer executedQuantity,
            SecurityDto security
    ) {
        TransactionSagaContext ctx = new TransactionSagaContext();
        ctx.sagaId = UUID.randomUUID();

        ctx.moneyOpId = UUID.randomUUID();
        ctx.releaseOpId = UUID.randomUUID();
        ctx.securityOpId = UUID.randomUUID();

        ctx.rollbackMoneyOpId = UUID.randomUUID();
        ctx.rollbackReserveOpId = UUID.randomUUID();
        ctx.rollbackSecurityOpId = UUID.randomUUID();

        ctx.buyerId = buyOrder.getCustomerAccountId();
        ctx.sellerId = sellOrder.getCustomerAccountId();
        ctx.securityId = sellOrder.getSecurityId();
        ctx.currencyId = CurrencyCodeEnum.getIdFromCode(security.getCurrencyCode());

        ctx.buyOrderId = buyOrder.getId();
        ctx.sellOrderId = sellOrder.getId();

        ctx.executedPrice = executedPrice;
        ctx.quantity = executedQuantity;

        ctx.feePerUnit = FeeCalculator.calculateFeePerUnit(executedPrice);
        ctx.totalFee = ctx.feePerUnit.multiply(BigDecimal.valueOf(executedQuantity));
        ctx.transferAmount = executedPrice.multiply(BigDecimal.valueOf(executedQuantity));
        ctx.releaseAmount = buyOrder.getPrice()
                .add(buyOrder.getFee())
                .multiply(BigDecimal.valueOf(executedQuantity));

        return ctx;
    }
}
