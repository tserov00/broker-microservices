package org.example.orderservice.service;

import jakarta.transaction.Transactional;
import org.example.orderservice.client.SavingsAccountClient;
import org.example.orderservice.client.SecurityClient;
import org.example.orderservice.dto.request.OrderDto;
import org.example.orderservice.dto.request.SavingsAccountDto;
import org.example.orderservice.dto.request.SecurityDto;
import org.example.orderservice.dto.response.OrderResponseDto;
import org.example.orderservice.dto.request.ReserveAmountRequestDto;
import org.example.orderservice.entity.*;
import org.example.orderservice.enumeration.CurrencyCodeEnum;
import org.example.orderservice.enumeration.OrderStatusEnum;
import org.example.orderservice.enumeration.OrderTypeEnum;
import org.example.orderservice.exception.custom.InsufficientSecurityException;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.*;
import org.example.orderservice.entity.CustomerPortfolio;
import org.example.orderservice.entity.Order;
import org.example.orderservice.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final SavingsAccountClient savingsAccountClient;
    private final SecurityClient securityClient;
    private final OrderRepository orderRepository;
    private final CustomerPortfolioRepository portfolioRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderTypeRepository orderTypeRepository;
    private final TransactionRepository transactionRepository;
    private final OrderMapper orderMapper;
    private final OrderMatchingService orderMatchingService;

    public OrderService(SavingsAccountClient savingsAccountClient, SecurityClient securityClient, OrderRepository orderRepository, CustomerPortfolioRepository portfolioRepository, OrderStatusRepository orderStatusRepository, OrderTypeRepository orderTypeRepository, TransactionRepository transactionRepository, OrderMapper orderMapper, OrderMatchingService orderMatchingService) {
        this.savingsAccountClient = savingsAccountClient;
        this.securityClient = securityClient;
        this.orderRepository = orderRepository;
        this.portfolioRepository = portfolioRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderTypeRepository = orderTypeRepository;
        this.transactionRepository = transactionRepository;
        this.orderMapper = orderMapper;
        this.orderMatchingService = orderMatchingService;
    }

    @Transactional
    public void createBuyOrder(OrderDto buyOrder, Long userId) {
        SecurityDto security = securityClient.getById(buyOrder.getSecurityId());
        Long currencyId = CurrencyCodeEnum.getIdFromCode(security.getCurrencyCode());
        SavingsAccountDto savingsAccount = savingsAccountClient.getByCurrency(userId, currencyId);

        BigDecimal feePerUnit = FeeCalculator.calculateFeePerUnit(buyOrder.getPrice());
        BigDecimal totalFee = feePerUnit.multiply(BigDecimal.valueOf(buyOrder.getQuantity()));

        BigDecimal totalCost = buyOrder.getPrice()
                .multiply(BigDecimal.valueOf(buyOrder.getQuantity()))
                .add(totalFee);

        ReserveAmountRequestDto reserveDto = new ReserveAmountRequestDto(userId, currencyId, totalCost);
        Order resultOrder = new Order(
                userId, security.getId(), savingsAccount.getId(), buyOrder.getPrice(),
                feePerUnit, buyOrder.getQuantity(), buyOrder.getQuantity(), LocalDateTime.now(),
                orderTypeRepository.findByType(OrderTypeEnum.BUY).orElseThrow(RuntimeException::new),
                orderStatusRepository.findByStatus(OrderStatusEnum.OPEN).orElseThrow(RuntimeException::new));

        try {
            savingsAccountClient.reserveAmount(reserveDto, UUID.randomUUID());
            orderRepository.save(resultOrder);
        } catch (Exception e) {
            savingsAccountClient.releaseAmount(reserveDto, UUID.randomUUID());
            throw e;
        }

        orderMatchingService.matchOrder(resultOrder);
    }

    @Transactional
    public void createSellOrder(OrderDto sellOrder, Long userId) {
        SecurityDto security = securityClient.getById(sellOrder.getSecurityId());
        Long currencyId = CurrencyCodeEnum.getIdFromCode(security.getCurrencyCode());
        SavingsAccountDto savingsAccount = savingsAccountClient.getByCurrency(userId, currencyId);
        CustomerPortfolio portfolio = portfolioRepository
                .findByCustomerAccountIdAndSecurityId(userId, security.getId())
                .orElseThrow(() -> new InsufficientSecurityException("Недостаточно ценных бумаг для прождажи"));

        if (portfolio.getTotalQuantity() - portfolio.getReservedQuantity() < sellOrder.getQuantity()) {
            throw new InsufficientSecurityException("Недостаточно ценных бумаг для продажи");
        }

        BigDecimal feePerUnit = FeeCalculator.calculateFeePerUnit(sellOrder.getPrice());

        portfolio.setReservedQuantity(portfolio.getReservedQuantity() + sellOrder.getQuantity());

        Order resultOrder = new Order(
                userId, security.getId(), savingsAccount.getId(), sellOrder.getPrice(),
                feePerUnit, sellOrder.getQuantity(), sellOrder.getQuantity(), LocalDateTime.now(),
                orderTypeRepository.findByType(OrderTypeEnum.SELL).orElseThrow(RuntimeException::new),
                orderStatusRepository.findByStatus(OrderStatusEnum.OPEN).orElseThrow(RuntimeException::new));

        orderRepository.save(resultOrder);
        portfolioRepository.save(portfolio);
        orderMatchingService.matchOrder(resultOrder);
    }

    public List<OrderResponseDto> getHistory(Long userId) {
        List<Order> orders = orderRepository.findAllByCustomerAccountId(userId);
        List<OrderResponseDto> responseList = new ArrayList<>();
        for (Order order : orders) {
            SecurityDto security = securityClient.getById(order.getSecurityId());
            responseList.add(orderMapper.toOrderResponseDto(order, security));
        }
        return responseList;
    }
}
