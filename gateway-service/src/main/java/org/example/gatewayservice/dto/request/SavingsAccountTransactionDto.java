package org.example.gatewayservice.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class SavingsAccountTransactionDto {

    @Size(min = 3, max = 3, message = "Код должен состоять из трех букв")
    private String currencyCode;

    @Positive(message = "Сумма должна быть больше 0")
    private BigDecimal amount;

    public SavingsAccountTransactionDto() {}

    public SavingsAccountTransactionDto(String currencyCode, BigDecimal amount) {
        this.currencyCode = currencyCode;
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
