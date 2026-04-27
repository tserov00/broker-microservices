package org.example.marketservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class FinnhubResponseDto {

    @JsonProperty("c")
    private BigDecimal lastPrice;

    public BigDecimal getLastPrice() {
        return lastPrice;
    }
}
