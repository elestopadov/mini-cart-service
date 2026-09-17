package com.example.minicart.integration;

import com.example.minicart.model.Cart;
import com.example.minicart.model.Category;
import com.example.minicart.model.Product;
import com.example.minicart.model.PromoCode;
import com.example.minicart.repository.FileCartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileCartRepositoryIT {
    @TempDir Path tempDir;

    @Test void save_and_load_cart_roundtrip() {
        FileCartRepository repo = new FileCartRepository(tempDir, new ObjectMapper());
        Cart original = new Cart("cart-1");
        original.addItem(new Product("p1", "Book", new BigDecimal("25.00"), Category.BOOKS), 2);
        original.setPromoCode(new PromoCode("BOOK5", new BigDecimal("5"), new BigDecimal("20")));
        repo.save(original);

        Cart loaded = repo.findById("cart-1").orElseThrow();
        assertThat(loaded.getId()).isEqualTo(original.getId());
        assertThat(loaded.getItems()).containsExactlyElementsOf(original.getItems());
        assertThat(loaded.getPromoCode()).isEqualTo(original.getPromoCode());
    }

    @Test void find_all_returns_saved_carts_in_deterministic_order() {
        FileCartRepository repo = new FileCartRepository(tempDir, new ObjectMapper());
        repo.save(new Cart("cart-2"));
        repo.save(new Cart("cart-1"));

        assertThat(repo.findAll().stream().map(Cart::getId).toList()).containsExactly("cart-1", "cart-2");
        assertThat(repo.findById("missing")).isEmpty();
    }

    @Test void corrupt_json_is_reported_and_path_traversal_is_rejected() throws Exception {
        FileCartRepository repo = new FileCartRepository(tempDir, new ObjectMapper());
        Files.writeString(tempDir.resolve("broken.json"), "{not-json}");
        assertThatThrownBy(repo::findAll).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> repo.findById("../secret")).isInstanceOf(IllegalArgumentException.class);
    }
}
