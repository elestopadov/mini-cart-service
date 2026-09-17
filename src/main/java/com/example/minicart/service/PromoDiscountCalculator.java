package com.example.minicart.service;

import com.example.minicart.exception.InvalidPromoCodeException;
import com.example.minicart.model.PromoCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PromoDiscountCalculator {
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    public BigDecimal calculate(BigDecimal subtotal, PromoCode promoCode) {
        Objects.requireNonNull(subtotal, "subtotal must not be null");
        if (promoCode == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (subtotal.compareTo(promoCode.minAmount()) < 0) {
            throw new InvalidPromoCodeException(promoCode.code());
        }

        return subtotal.multiply(promoCode.discountPercent())
                .divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }
}
