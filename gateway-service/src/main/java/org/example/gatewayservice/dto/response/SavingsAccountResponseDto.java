package org.example.gatewayservice.dto.response;

import java.math.BigDecimal;

public class SavingsAccountResponseDto {
    private String currencyCode;
    private BigDecimal balance;
    private BigDecimal reservedAmount;

    public SavingsAccountResponseDto() {}

    public SavingsAccountResponseDto(String currencyCode, BigDecimal balance, BigDecimal reservedAmount) {
        this.currencyCode = currencyCode;
        this.balance = balance;
        this.reservedAmount = reservedAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public void setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }
}
