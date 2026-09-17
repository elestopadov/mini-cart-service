package com.example.minicart.service;

import com.example.minicart.model.Cart;
import com.example.minicart.model.CartItem;
import com.example.minicart.model.Category;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class DiscountService {
    private final PromoDiscountCalculator promoDiscountCalculator;

    public DiscountService() { this(new PromoDiscountCalculator()); }

    public DiscountService(PromoDiscountCalculator promoDiscountCalculator) {
        this.promoDiscountCalculator = Objects.requireNonNull(promoDiscountCalculator);
    }

    public BigDecimal calculateDiscount(Cart cart) {
        Objects.requireNonNull(cart, "cart must not be null");
        BigDecimal subtotal = cart.subtotal();

        BigDecimal categoryDiscount = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            categoryDiscount = categoryDiscount.add(
                    item.subtotal().multiply(categoryRate(item.product().category()))
            );
        }

        BigDecimal thresholdDiscount = thresholdDiscount(subtotal);
        BigDecimal promoDiscount = BigDecimal.ZERO;
        if (cart.getPromoCode() != null) {
            promoDiscount = promoDiscountCalculator.calculate(subtotal, cart.getPromoCode());
        }

        return categoryDiscount.add(thresholdDiscount).add(promoDiscount)
                .min(subtotal.max(BigDecimal.ZERO))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal categoryRate(Category category) {
        return switch (category) {
            case ELECTRONICS -> BigDecimal.valueOf(0.05);
            case CLOTHING -> BigDecimal.valueOf(0.10);
            case FOOD -> BigDecimal.ZERO;
            case BOOKS -> BigDecimal.valueOf(0.15);
        };
    }

    private BigDecimal thresholdDiscount(BigDecimal subtotal) {
        if (subtotal.compareTo(BigDecimal.valueOf(500)) >= 0) {
            return subtotal.multiply(BigDecimal.valueOf(0.07));
        }
        if (subtotal.compareTo(BigDecimal.valueOf(100)) >= 0) {
            return subtotal.multiply(BigDecimal.valueOf(0.03));
        }
        return BigDecimal.ZERO;
    }
}
