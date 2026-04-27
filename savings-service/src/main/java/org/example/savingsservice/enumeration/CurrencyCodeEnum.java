package org.example.savingsservice.enumeration;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum CurrencyCodeEnum {
    USD(1L, "USD"),
    EUR(2L, "EUR"),
    GBP(3L, "GBP"),
    JPY(4L, "JPY"),
    CHF(5L, "CHF"),
    RUB(6L, "RUB");

    private final Long id;
    private final String code;

    CurrencyCodeEnum(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    private static final Map<Long, CurrencyCodeEnum> BY_ID =
            Arrays.stream(values())
                    .collect(Collectors.toMap(CurrencyCodeEnum::getId, e -> e));

    public static CurrencyCodeEnum fromId(Long id) {
        CurrencyCodeEnum result = BY_ID.get(id);
        if (result == null) {
            throw new IllegalArgumentException("Unknown currencyId: " + id);
        }
        return result;
    }
}
