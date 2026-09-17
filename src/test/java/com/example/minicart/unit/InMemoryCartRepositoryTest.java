package com.example.minicart.unit;

import com.example.minicart.model.Cart;
import com.example.minicart.model.Category;
import com.example.minicart.repository.InMemoryCartRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryCartRepositoryTest {
    @Test void repository_uses_defensive_copies() {
        InMemoryCartRepository repo = new InMemoryCartRepository();
        Cart original = new Cart("c1");
        original.addItem(TestData.product("p1", "10", Category.FOOD), 1);
        repo.save(original);

        original.clear();
        assertThat(repo.findById("c1").orElseThrow().getItems()).hasSize(1);

        Cart loaded = repo.findById("c1").orElseThrow();
        loaded.clear();
        assertThat(repo.findAll()).singleElement().satisfies(c -> assertThat(c.getItems()).hasSize(1));
    }
}
