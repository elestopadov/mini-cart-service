package com.example.minicart.service;

import com.example.minicart.model.Category;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class TaxService {
    public BigDecimal calculateTax(BigDecimal amount, Category category) {
        Objects.requireNonNull(amount, "amount must not be null");
        Objects.requireNonNull(category, "category must not be null");
        if (amount.signum() < 0) throw new IllegalArgumentException("amount must not be negative");

        BigDecimal rate = switch (category) {
            case FOOD -> BigDecimal.valueOf(0.10);
            case BOOKS -> BigDecimal.ZERO;
            case ELECTRONICS, CLOTHING -> BigDecimal.valueOf(0.20);
        };
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
