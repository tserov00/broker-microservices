package org.example.savingsservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SavingsAccountHistoryResponseDto {

    private BigDecimal amount;

    private String currencyCode;

    private String transactionType;

    private LocalDateTime transactionDate;

    public SavingsAccountHistoryResponseDto(BigDecimal amount, String currencyCode, String transactionType, LocalDateTime transactionDate) {
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
