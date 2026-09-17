package com.example.minicart.unit;

import com.example.minicart.exception.InvalidPromoCodeException;
import com.example.minicart.model.PromoCode;
import com.example.minicart.service.PromoDiscountCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PromoDiscountCalculatorTest {
    private final PromoDiscountCalculator calculator = new PromoDiscountCalculator();

    // This test uniquely covers the null-promo branch used by -Pcoverage-demo.
    @Test void null_promo_code_returns_zero() {
        assertThat(calculator.calculate(new BigDecimal("100"), null)).isEqualByComparingTo("0.00");
    }
    @Test void valid_promo_code_returns_discount() {
        PromoCode promo = new PromoCode("SAVE10", new BigDecimal("10"), new BigDecimal("100"));
        assertThat(calculator.calculate(new BigDecimal("150"), promo)).isEqualByComparingTo("15.00");
    }
    @Test void promo_code_below_minimum_throws() {
        PromoCode promo = new PromoCode("SAVE10", new BigDecimal("10"), new BigDecimal("100"));
        assertThatThrownBy(() -> calculator.calculate(new BigDecimal("99"), promo)).isInstanceOf(InvalidPromoCodeException.class);
    }
}
