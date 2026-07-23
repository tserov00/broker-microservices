package org.example.orderservice.dto.request;

import org.example.orderservice.enumeration.CurrencyCodeEnum;

public class CurrencyDto {
    private Long id;
    private CurrencyCodeEnum code;

    public CurrencyDto() {}

    public CurrencyDto(Long id, CurrencyCodeEnum code) {
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
