package com.example.minicart.integration;

import com.example.minicart.model.Product;
import com.example.minicart.repository.FileCartRepository;
import com.example.minicart.service.CartService;
import com.example.minicart.service.DiscountService;
import com.example.minicart.service.TaxService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartServiceIT {
    @TempDir Path tempDir;

    @Test void full_checkout_persists_cart_snapshot_to_file() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FileCartRepository repository = new FileCartRepository(tempDir, mapper);
        CartService service = new CartService(repository, new DiscountService(), new TaxService());
        String cartId = service.createCart();

        Product product;
        try (InputStream input = getClass().getResourceAsStream("/fixtures/products.json")) {
            List<Product> products = mapper.readValue(input, new TypeReference<>() {});
            product = products.stream().filter(p -> p.id().equals("p-100")).findFirst().orElseThrow();
        }

        service.addItem(cartId, product, 1);
        service.checkout(cartId);

        assertThat(Files.exists(tempDir.resolve(cartId + ".json"))).isTrue();
        assertThat(repository.findById(cartId).orElseThrow().getItems()).hasSize(1);
    }
}
