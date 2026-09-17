package com.example.minicart.repository.dto;

import com.example.minicart.model.Cart;
import com.example.minicart.model.PromoCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private String id;
    private List<CartItemDto> items = new ArrayList<>();
    private PromoCode promoCode;

    public static CartDto from(Cart cart) {
        return new CartDto(cart.getId(), cart.getItems().stream().map(CartItemDto::from).toList(), cart.getPromoCode());
    }

    public Cart toDomain() {
        Cart cart = new Cart(id);
        items.forEach(item -> cart.addItem(item.toDomain()));
        cart.setPromoCode(promoCode);
        return cart;
    }
}
