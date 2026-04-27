package org.example.orderserivce.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FeeCalculator {

    private FeeCalculator() {}

    public static BigDecimal calculateFeePerUnit(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        BigDecimal fee = price.multiply(new BigDecimal("0.003"));
        return fee.setScale(2, RoundingMode.HALF_UP);
    }
}