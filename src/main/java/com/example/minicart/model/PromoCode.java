package com.example.minicart.model;

import java.math.BigDecimal;
import java.util.Objects;

public record PromoCode(String code, BigDecimal discountPercent, BigDecimal minAmount) {
    public PromoCode {
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(discountPercent, "discountPercent must not be null");
        Objects.requireNonNull(minAmount, "minAmount must not be null");
        if (code.isBlank()) throw new IllegalArgumentException("code must not be blank");
        if (discountPercent.compareTo(BigDecimal.ZERO) < 0 || discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("discountPercent must be between 0 and 100");
        }
        if (minAmount.signum() < 0) throw new IllegalArgumentException("minAmount must not be negative");
    }
}
