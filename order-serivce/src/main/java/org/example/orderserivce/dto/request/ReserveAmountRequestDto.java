package org.example.orderserivce.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ReserveAmountRequestDto {
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("currencyId")
    private Long currencyId;

    @JsonProperty("amount")
    private BigDecimal amount;

    public ReserveAmountRequestDto() {}

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ReserveAmountRequestDto(
            @JsonProperty("userId") Long userId,
            @JsonProperty("currencyId") Long currencyId,
            @JsonProperty("amount") BigDecimal amount) {
        this.userId = userId;
        this.currencyId = currencyId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
