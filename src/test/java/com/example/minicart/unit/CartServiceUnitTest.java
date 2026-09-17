package com.example.minicart.unit;

import com.example.minicart.exception.CartNotFoundException;
import com.example.minicart.exception.EmptyCartException;
import com.example.minicart.exception.ProductNotFoundException;
import com.example.minicart.model.Cart;
import com.example.minicart.model.Category;
import com.example.minicart.model.Product;
import com.example.minicart.repository.CartRepository;
import com.example.minicart.service.CartService;
import com.example.minicart.service.DiscountService;
import com.example.minicart.service.TaxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceUnitTest {
    @Mock CartRepository repository;
    @Mock DiscountService discountService;
    @Mock TaxService taxService;

    @Test void add_item_saves_updated_cart() {
        Cart cart = new Cart("c1");
        when(repository.findById("c1")).thenReturn(Optional.of(cart));
        CartService service = new CartService(repository, discountService, taxService);
        service.addItem("c1", TestData.product("p1", "20", Category.FOOD), 2);
        assertThat(cart.getItems()).hasSize(1);
        verify(repository).save(cart);
    }

    @Test void remove_item_removes_existing_product_and_saves() {
        Cart cart = new Cart("c1");
        cart.addItem(TestData.product("p1", "20", Category.FOOD), 1);
        when(repository.findById("c1")).thenReturn(Optional.of(cart));
        CartService service = new CartService(repository, discountService, taxService);
        service.removeItem("c1", "p1");
        assertThat(cart.getItems()).isEmpty();
        verify(repository).save(cart);
    }

    @Test void get_total_combines_discount_and_tax() {
        Cart cart = new Cart("c1");
        cart.addItem(TestData.product("p1", "100", Category.FOOD), 1);
        when(repository.findById("c1")).thenReturn(Optional.of(cart));
        when(discountService.calculateDiscount(cart)).thenReturn(new BigDecimal("10.00"));
        when(taxService.calculateTax(any(BigDecimal.class), eq(Category.FOOD))).thenReturn(new BigDecimal("9.00"));
        CartService service = new CartService(repository, discountService, taxService);

        assertThat(service.getTotal("c1")).isEqualByComparingTo("99.00");
        verify(discountService).calculateDiscount(cart);
        verify(taxService).calculateTax(new BigDecimal("90.000000000000"), Category.FOOD);
    }

    @Test void checkout_persists_snapshot_and_clears_working_cart() {
        Cart cart = new Cart("c1");
        cart.addItem(TestData.product("p1", "20", Category.BOOKS), 1);
        when(repository.findById("c1")).thenReturn(Optional.of(cart));
        CartService service = new CartService(repository, discountService, taxService);

        service.checkout("c1");

        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getItems()).hasSize(1);
        assertThat(cart.getItems()).isEmpty();
    }

    @Test void invalid_cart_operations_throw_expected_exceptions() {
        when(repository.findById("missing")).thenReturn(Optional.empty());
        CartService service = new CartService(repository, discountService, taxService);
        assertThatThrownBy(() -> service.getTotal("missing")).isInstanceOf(CartNotFoundException.class);

        Cart empty = new Cart("empty");
        when(repository.findById("empty")).thenReturn(Optional.of(empty));
        assertThatThrownBy(() -> service.checkout("empty")).isInstanceOf(EmptyCartException.class);
        assertThatThrownBy(() -> service.removeItem("empty", "p1")).isInstanceOf(ProductNotFoundException.class);
    }
}
