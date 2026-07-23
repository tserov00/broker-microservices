package org.example.orderservice.service;

import org.example.orderservice.repository.*;
import org.example.orderservice.saga.TransactionSagaContext;
import org.example.orderservice.saga.TransactionSagaOrchestrator;
import org.example.orderservice.client.SavingsAccountClient;
import org.example.orderservice.client.SecurityClient;
import org.example.orderservice.dto.request.SecurityDto;
import org.example.orderservice.entity.Order;
import org.example.orderservice.enumeration.CurrencyCodeEnum;
import org.example.orderservice.enumeration.OrderTypeEnum;
import org.example.orderservice.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
