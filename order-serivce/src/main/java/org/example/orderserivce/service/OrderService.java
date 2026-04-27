package org.example.orderserivce.service;

import jakarta.transaction.Transactional;
import org.example.orderserivce.client.CurrencyClient;
import org.example.orderserivce.client.SavingsAccountClient;
import org.example.orderserivce.client.SecurityClient;
import org.example.orderserivce.dto.request.OrderDto;
import org.example.orderserivce.dto.request.SavingsAccountDto;
import org.example.orderserivce.dto.request.SecurityDto;
import org.example.orderserivce.dto.response.OrderResponseDto;
import org.example.orderserivce.dto.request.ReserveAmountRequestDto;
import org.example.orderserivce.dto.response.TransferResponseDto;
import org.example.orderserivce.entity.*;
import org.example.orderserivce.enumeration.CurrencyCodeEnum;
import org.example.orderserivce.enumeration.OrderStatusEnum;
import org.example.orderserivce.enumeration.OrderTypeEnum;
import org.example.orderserivce.exception.custom.InsufficientSecurityException;
import org.example.orderserivce.mapper.OrderMapper;
import org.example.orderserivce.mapper.TransactionBuyMapper;
import org.example.orderserivce.mapper.TransactionSellMapper;
import org.example.orderserivce.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
