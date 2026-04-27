package org.example.savingsservice.dto.response;

import org.example.savingsservice.enumeration.CurrencyCodeEnum;

public class CurrencyResponseDto {
    private Long id;
    private CurrencyCodeEnum code;

    public CurrencyResponseDto() {}

    public CurrencyResponseDto(Long id, CurrencyCodeEnum code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyCodeEnum getCode() {
        return code;
    }

    public void setCode(CurrencyCodeEnum code) {
        this.code = code;
    }
}
