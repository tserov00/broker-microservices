package org.example.orderserivce.service;

import jakarta.transaction.Transactional;
import org.example.orderserivce.client.SecurityClient;
import org.example.orderserivce.dto.request.SecurityDto;
import org.example.orderserivce.dto.response.PortfolioResponseDto;
import org.example.orderserivce.entity.CustomerPortfolio;
import org.example.orderserivce.exception.custom.InsufficientSecurityException;
import org.example.orderserivce.mapper.CustomerPortfolioMapper;
import org.example.orderserivce.repository.CustomerPortfolioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerPortfolioService {

    private final CustomerPortfolioMapper portfolioMapper;
    private final SecurityClient securityClient;
    private final CustomerPortfolioRepository customerPortfolioRepository;
    private final IdempotencyService idempotencyService;

    public CustomerPortfolioService(CustomerPortfolioMapper portfolioMapper, SecurityClient securityClient, CustomerPortfolioRepository customerPortfolioRepository, IdempotencyService idempotencyService) {
        this.portfolioMapper = portfolioMapper;
        this.securityClient = securityClient;
        this.customerPortfolioRepository = customerPortfolioRepository;
        this.idempotencyService = idempotencyService;
    }

    public List<PortfolioResponseDto> getPortfolio(Long userId) {

        List<CustomerPortfolio> portfolio =
                customerPortfolioRepository.findAllByCustomerAccountId(userId);

        if (portfolio.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> securityIds = portfolio.stream()
                .map(CustomerPortfolio::getSecurityId)
                .distinct()
                .toList();

        List<SecurityDto> securities = securityClient.getByIds(securityIds);

        Map<Long, SecurityDto> securityMap = securities.stream()
                .collect(Collectors.toMap(SecurityDto::getId, s -> s));

        List<PortfolioResponseDto> responseList = new ArrayList<>();

        for (CustomerPortfolio portfolioItem : portfolio) {

            SecurityDto security = securityMap.get(portfolioItem.getSecurityId());

            if (security == null) {
                continue;
            }

            PortfolioResponseDto dto =
                    portfolioMapper.toResponseDto(portfolioItem, security);

            BigDecimal profitPerUnit = security.getLastPrice()
                    .subtract(portfolioItem.getAvgBuyPrice());

            BigDecimal totalProfit = profitPerUnit
                    .multiply(BigDecimal.valueOf(portfolioItem.getTotalQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);

            dto.setUnrealizedProfit(totalProfit);

            responseList.add(dto);
        }

        return responseList;
    }

    @Transactional
    public void transferSecurity(
            Long securityId,
            Long buyerId,
            Long sellerId,
            Integer securityQuantity,
            BigDecimal executedPrice,
            BigDecimal feePerUnit,
            UUID requestId
    ) {
        idempotencyService.checkAndMark(requestId);

        CustomerPortfolio buyerPortfolio = customerPortfolioRepository
                .findByCustomerAccountIdAndSecurityId(buyerId, securityId)
                .orElseGet(() -> {
                    CustomerPortfolio p = new CustomerPortfolio();
                    p.setCustomerAccountId(buyerId);
                    p.setSecurityId(securityId);
                    p.setTotalQuantity(0);
                    return customerPortfolioRepository.save(p);
                });

        CustomerPortfolio sellerPortfolio = customerPortfolioRepository
                .findByCustomerAccountIdAndSecurityId(sellerId, securityId)
                .orElseThrow(RuntimeException::new);

        int sellerTotalQuantity = sellerPortfolio.getTotalQuantity() == null ? 0 : sellerPortfolio.getTotalQuantity();
        int sellerReservedQuantity = sellerPortfolio.getReservedQuantity() == null ? 0 : sellerPortfolio.getReservedQuantity();

        int buyerTotalQuantity = buyerPortfolio.getTotalQuantity() == null ? 0 : buyerPortfolio.getTotalQuantity();
        BigDecimal buyerAvgBuyPrice = buyerPortfolio.getAvgBuyPrice();

        int buyerNewQuantity = securityQuantity;
        int sellerNewQuantity = securityQuantity;

        if (sellerReservedQuantity < securityQuantity) {
            throw new InsufficientSecurityException("Недостаточно зарезервированных бумаг");
        }

        BigDecimal effectiveBuyPrice = executedPrice.add(feePerUnit);

        if (buyerAvgBuyPrice == null || buyerTotalQuantity == 0) {
            buyerPortfolio.setAvgBuyPrice(effectiveBuyPrice);
            buyerPortfolio.setTotalQuantity(buyerNewQuantity);
        } else {
            BigDecimal newAvgBuyPrice =
                    buyerAvgBuyPrice.multiply(BigDecimal.valueOf(buyerTotalQuantity))
                            .add(effectiveBuyPrice.multiply(BigDecimal.valueOf(buyerNewQuantity)))
                            .divide(
                                    BigDecimal.valueOf(buyerTotalQuantity + buyerNewQuantity),
                                    8,
                                    RoundingMode.HALF_UP
                            );

            buyerPortfolio.setAvgBuyPrice(newAvgBuyPrice);
            buyerPortfolio.setTotalQuantity(buyerTotalQuantity + buyerNewQuantity);
        }

        BigDecimal sellerAvgSellPrice = sellerPortfolio.getAvgSellPrice();
        Integer sellerSoldQuantity = sellerPortfolio.getSoldQuantity() == null ? 0 : sellerPortfolio.getSoldQuantity();

        BigDecimal effectiveSellPrice = executedPrice.subtract(feePerUnit);

        if (sellerAvgSellPrice == null || sellerSoldQuantity == 0) {
            sellerPortfolio.setAvgSellPrice(effectiveSellPrice);
            sellerPortfolio.setSoldQuantity(sellerNewQuantity);
        } else {
            BigDecimal newAvgSellPrice =
                    sellerAvgSellPrice.multiply(BigDecimal.valueOf(sellerSoldQuantity))
                            .add(effectiveSellPrice.multiply(BigDecimal.valueOf(sellerNewQuantity)))
                            .divide(
                                    BigDecimal.valueOf(sellerSoldQuantity + sellerNewQuantity),
                                    8,
                                    RoundingMode.HALF_UP
                            );

            sellerPortfolio.setAvgSellPrice(newAvgSellPrice);
            sellerPortfolio.setSoldQuantity(sellerSoldQuantity + sellerNewQuantity);
        }

        sellerPortfolio.setTotalQuantity(sellerTotalQuantity - securityQuantity);
        sellerPortfolio.setReservedQuantity(sellerReservedQuantity - securityQuantity);

        customerPortfolioRepository.save(sellerPortfolio);
        customerPortfolioRepository.save(buyerPortfolio);
    }
}
