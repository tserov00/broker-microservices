package org.example.savingsservice.dto.response;

import java.math.BigDecimal;

public class SavingsAccountEntityResponseDto {

    private Long id;
    private Long accountId;
    private String savingsAccountNumber;
    private Long currencyId;
    private BigDecimal balance;
    private BigDecimal reservedAmount;

    public SavingsAccountEntityResponseDto() {}

    public SavingsAccountEntityResponseDto(Long id, Long accountId, String savingsAccountNumber, Long currencyId, BigDecimal balance, BigDecimal reservedAmount) {
        this.id = id;
        this.accountId = accountId;
        this.savingsAccountNumber = savingsAccountNumber;
        this.currencyId = currencyId;
        this.balance = balance;
        this.reservedAmount = reservedAmount;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getSavingsAccountNumber() {
        return savingsAccountNumber;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getReservedAmount() {
        return reservedAmount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setSavingsAccountNumber(String savingsAccountNumber) {
        this.savingsAccountNumber = savingsAccountNumber;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setReservedAmount(BigDecimal reservedAmount) {
        this.reservedAmount = reservedAmount;
    }
}
