package org.example.orderserivce.dto.response;

import java.math.BigDecimal;

public class TransactionResponseDto {
    private String ticker;
    private String transactionType;
    private BigDecimal executedPrice;
    private BigDecimal executedFee;
    private Integer quantity;
    private Long orderId;

    public TransactionResponseDto() {}

    public TransactionResponseDto(String ticker, String transactionType, BigDecimal executedPrice, BigDecimal executedFee, Integer quantity, Long orderId) {
        this.ticker = ticker;
        this.transactionType = transactionType;
        this.executedPrice = executedPrice;
        this.executedFee = executedFee;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(BigDecimal executedPrice) {
        this.executedPrice = executedPrice;
    }

    public BigDecimal getExecutedFee() {
        return executedFee;
    }

    public void setExecutedFee(BigDecimal executedFee) {
        this.executedFee = executedFee;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
