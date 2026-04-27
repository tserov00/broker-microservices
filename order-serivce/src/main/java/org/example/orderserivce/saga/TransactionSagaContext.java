package org.example.orderserivce.saga;

import org.example.orderserivce.dto.request.SecurityDto;
import org.example.orderserivce.entity.Order;
import org.example.orderserivce.enumeration.CurrencyCodeEnum;
import org.example.orderserivce.service.FeeCalculator;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionSagaContext {

    public UUID sagaId;

    public UUID moneyOpId;
    public UUID releaseOpId;
    public UUID securityOpId;

    public UUID rollbackMoneyOpId;
    public UUID rollbackReserveOpId;
    public UUID rollbackSecurityOpId;

    public Long buyerId;
    public Long sellerId;
    public Long securityId;
    public Long currencyId;

    public Long buyOrderId;
    public Long sellOrderId;

    public BigDecimal executedPrice;
    public Integer quantity;

    public BigDecimal feePerUnit;
    public BigDecimal totalFee;
    public BigDecimal transferAmount;
    public BigDecimal releaseAmount;
}
