package com.example.minicart.unit;

import com.example.minicart.model.Category;
import com.example.minicart.service.TaxService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaxServiceTest {
    private final TaxService service = new TaxService();

    @Test void food_tax_is_10_percent() { assertThat(service.calculateTax(new BigDecimal("100"), Category.FOOD)).isEqualByComparingTo("10.00"); }
    @Test void books_tax_is_zero() { assertThat(service.calculateTax(new BigDecimal("100"), Category.BOOKS)).isEqualByComparingTo("0.00"); }
    @Test void electronics_tax_is_20_percent() { assertThat(service.calculateTax(new BigDecimal("100"), Category.ELECTRONICS)).isEqualByComparingTo("20.00"); }
    @Test void negative_tax_base_is_rejected() { assertThatThrownBy(() -> service.calculateTax(new BigDecimal("-1"), Category.FOOD)).isInstanceOf(IllegalArgumentException.class); }
}
