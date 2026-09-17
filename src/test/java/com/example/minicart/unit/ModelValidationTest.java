package com.example.minicart.unit;

import com.example.minicart.model.CartItem;
import com.example.minicart.model.Category;
import com.example.minicart.model.Product;
import com.example.minicart.model.PromoCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ModelValidationTest {
    @Test void product_rejects_invalid_values() {
        assertThatThrownBy(() -> new Product("", "name", new BigDecimal("1"), Category.FOOD)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Product("p1", "", new BigDecimal("1"), Category.FOOD)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Product("p1", "name", BigDecimal.ZERO, Category.FOOD)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void cart_item_and_promo_code_reject_invalid_values() {
        Product product = TestData.product("p1", "1", Category.FOOD);
        assertThatThrownBy(() -> new CartItem(product, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new PromoCode("SAVE", new BigDecimal("101"), BigDecimal.ZERO)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new PromoCode("SAVE", BigDecimal.TEN, new BigDecimal("-1"))).isInstanceOf(IllegalArgumentException.class);
    }
}
