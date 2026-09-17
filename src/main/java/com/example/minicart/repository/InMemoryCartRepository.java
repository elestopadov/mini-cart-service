package com.example.minicart.repository;

import com.example.minicart.model.Cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;

public class InMemoryCartRepository implements CartRepository {
    private final Map<String, Cart> storage = new HashMap<>();

    @Override public void save(Cart cart) {
        Objects.requireNonNull(cart, "cart must not be null");
        storage.put(cart.getId(), cart.copy());
    }
    @Override public Optional<Cart> findById(String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(storage.get(id)).map(Cart::copy);
    }
    @Override public List<Cart> findAll() {
        return storage.values().stream()
                .sorted(java.util.Comparator.comparing(Cart::getId))
                .map(Cart::copy)
                .toList();
    }
}
