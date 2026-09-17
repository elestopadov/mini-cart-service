package com.example.minicart.model;

import java.math.BigDecimal;
import java.util.Objects;

public record Product(String id, String name, BigDecimal price, Category category) {
    public Product {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(price, "price must not be null");
        Objects.requireNonNull(category, "category must not be null");
        if (id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        if (name.isBlank()) throw new IllegalArgumentException("name must not be blank");
        if (price.signum() <= 0) throw new IllegalArgumentException("price must be positive");
    }
}
