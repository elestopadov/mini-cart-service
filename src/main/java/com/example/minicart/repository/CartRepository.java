package com.example.minicart.repository;

import com.example.minicart.model.Cart;
import java.util.List;
import java.util.Optional;

public interface CartRepository {
    void save(Cart cart);
    Optional<Cart> findById(String id);
    List<Cart> findAll();
}
