package com.example.minicart.repository.dto;

import com.example.minicart.model.CartItem;
import com.example.minicart.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Product product;
    private int quantity;

    public static CartItemDto from(CartItem item) { return new CartItemDto(item.product(), item.quantity()); }
    public CartItem toDomain() { return new CartItem(product, quantity); }
}
