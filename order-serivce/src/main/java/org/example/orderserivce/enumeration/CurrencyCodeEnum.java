package org.example.orderserivce.enumeration;

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

    private static final Map<String, CurrencyCodeEnum> BY_CODE =
            Arrays.stream(values())
                    .collect(Collectors.toMap(CurrencyCodeEnum::getCode, e -> e));

    public static CurrencyCodeEnum fromCode(String code) {
        CurrencyCodeEnum result = BY_CODE.get(code);
        if (result == null) {
            throw new IllegalArgumentException("Unknown currencyCode: " + code);
        }
        return result;
    }

    public static Long getIdFromCode(String code) {
        return fromCode(code).getId();
    }
}
