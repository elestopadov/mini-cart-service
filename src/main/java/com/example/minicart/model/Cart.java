package com.example.minicart.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Cart {
    private final String id;
    private final List<CartItem> items = new ArrayList<>();
    private PromoCode promoCode;

    public Cart(String id) {
        Objects.requireNonNull(id, "id must not be null");
        if (id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        this.id = id;
    }

    public String getId() { return id; }
    public List<CartItem> getItems() { return Collections.unmodifiableList(items); }
    public PromoCode getPromoCode() { return promoCode; }
    public void setPromoCode(PromoCode promoCode) { this.promoCode = promoCode; }

    public void addItem(Product product, int quantity) {
        addItem(new CartItem(product, quantity));
    }

    public void addItem(CartItem item) {
        items.add(Objects.requireNonNull(item, "item must not be null"));
    }

    public boolean removeItem(String productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return items.removeIf(item -> item.product().id().equals(productId));
    }

    public void clear() { items.clear(); }
    public boolean isEmpty() { return items.isEmpty(); }

    public BigDecimal subtotal() {
        return items.stream().map(CartItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Cart copy() {
        Cart copy = new Cart(id);
        items.forEach(copy::addItem);
        copy.setPromoCode(promoCode);
        return copy;
    }
}
