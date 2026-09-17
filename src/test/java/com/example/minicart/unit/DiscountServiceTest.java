package com.example.minicart.unit;

import com.example.minicart.model.Cart;
import com.example.minicart.model.Category;
import com.example.minicart.model.PromoCode;
import com.example.minicart.service.DiscountService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiscountServiceTest {
    private final DiscountService service = new DiscountService();

    @Test void electronics_discount_is_5_percent() {
        Cart cart = new Cart("c1");
        cart.addItem(TestData.product("p1", "20", Category.ELECTRONICS), 1);
        assertThat(service.calculateDiscount(cart)).isEqualByComparingTo("1.00");
    }
    @Test void clothing_discount_is_10_percent() {
        Cart cart = new Cart("c1");
        cart.addItem(TestData.product("p1", "20", Category.CLOTHING), 1);
        assertThat(service.calculateDiscount(cart)).isEqualByComparingTo("2.00");
    }
    @Test void books_discount_is_15_percent() {
        Cart cart = new Cart("c1");
        cart.addItem(TestData.product("p1", "20", Category.BOOKS), 1);
        assertThat(service.calculateDiscount(cart)).isEqualByComparingTo("3.00");
    }
    @Test void threshold_discount_is_3_percent_at_100() {
        Cart cart = new Cart("c1");
        cart.addItem(TestData.product("p1", "100", Category.FOOD), 1);
        assertThat(service.calculateDiscount(cart)).isEqualByComparingTo("3.00");
    }
    @Test void threshold_discount_is_7_percent_at_500() {
        Cart cart = new Cart("c1");
        cart.addItem(TestData.product("p1", "500", Category.FOOD), 1);
        assertThat(service.calculateDiscount(cart)).isEqualByComparingTo("35.00");
    }
    @Test void category_threshold_and_promo_discounts_are_combined_and_capped() {
        Cart cart = new Cart("c1");
        cart.addItem(TestData.product("p1", "100", Category.CLOTHING), 1);
        cart.setPromoCode(new PromoCode("SAVE10", new BigDecimal("10"), new BigDecimal("100")));
        assertThat(service.calculateDiscount(cart)).isEqualByComparingTo("23.00");

        Cart capped = new Cart("c2");
        capped.addItem(TestData.product("p2", "100", Category.BOOKS), 1);
        capped.setPromoCode(new PromoCode("FREE", new BigDecimal("100"), new BigDecimal("0")));
        assertThat(service.calculateDiscount(capped)).isEqualByComparingTo("100.00");
    }
}
