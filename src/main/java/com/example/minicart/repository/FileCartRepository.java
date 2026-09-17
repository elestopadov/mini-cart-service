package com.example.minicart.repository;

import com.example.minicart.model.Cart;
import com.example.minicart.repository.dto.CartDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileCartRepository implements CartRepository {
    private final Path directory;
    private final ObjectMapper objectMapper;

    public FileCartRepository(Path directory, ObjectMapper objectMapper) {
        this.directory = Objects.requireNonNull(directory, "directory must not be null").toAbsolutePath().normalize();
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create repository directory: " + directory, e);
        }
    }

    @Override
    public void save(Cart cart) {
        Objects.requireNonNull(cart, "cart must not be null");
        validateId(cart.getId());
        Path file = directory.resolve(cart.getId() + ".json");
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), CartDto.from(cart));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot save cart: " + cart.getId(), e);
        }
    }

    @Override
    public Optional<Cart> findById(String id) {
        validateId(id);
        Path file = directory.resolve(id + ".json");
        if (!Files.exists(file)) return Optional.empty();
        return Optional.of(read(file));
    }

    @Override
    public List<Cart> findAll() {
        try (var paths = Files.list(directory)) {
            return paths
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .sorted()
                    .map(this::read)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read repository directory: " + directory, e);
        }
    }

    private Cart read(Path file) {
        try {
            return objectMapper.readValue(file.toFile(), CartDto.class).toDomain();
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Cannot read cart file: " + file, e);
        }
    }

    private void validateId(String id) {
        if (id == null || id.isBlank() || !id.matches("[A-Za-z0-9._-]+")) {
            throw new IllegalArgumentException("Unsafe cart id: " + id);
        }
    }
}
