package com.example.minicart.service;

import com.example.minicart.exception.CartNotFoundException;
import com.example.minicart.exception.EmptyCartException;
import com.example.minicart.exception.ProductNotFoundException;
import com.example.minicart.model.Cart;
import com.example.minicart.model.CartItem;
import com.example.minicart.model.Product;
import com.example.minicart.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class CartService {
    private final CartRepository repository;
    private final DiscountService discountService;
    private final TaxService taxService;

    public CartService(CartRepository repository, DiscountService discountService, TaxService taxService) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.discountService = Objects.requireNonNull(discountService, "discountService must not be null");
        this.taxService = Objects.requireNonNull(taxService, "taxService must not be null");
    }

    public String createCart() {
        String id = UUID.randomUUID().toString();
        repository.save(new Cart(id));
        log.debug("Created cart {}", id);
        return id;
    }

    public void addItem(String cartId, Product product, int quantity) {
        Cart cart = getCart(cartId);
        cart.addItem(product, quantity);
        repository.save(cart);
    }

    public void removeItem(String cartId, String productId) {
        Cart cart = getCart(cartId);
        if (!cart.removeItem(productId)) {
            throw new ProductNotFoundException(productId);
        }
        repository.save(cart);
    }

    public BigDecimal getTotal(String cartId) {
        Cart cart = getCart(cartId);
        if (cart.isEmpty()) return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        BigDecimal subtotal = cart.subtotal();
        BigDecimal discount = discountService.calculateDiscount(cart);
        BigDecimal taxableTotal = subtotal.subtract(discount).max(BigDecimal.ZERO);
        BigDecimal tax = calculateTaxByLine(cart, discount, subtotal);
        return money(taxableTotal.add(tax));
    }

    public void checkout(String cartId) {
        Cart cart = getCart(cartId);
        if (cart.isEmpty()) throw new EmptyCartException(cartId);
        repository.save(cart.copy());
        cart.clear();
        log.info("Checked out cart {}", cartId);
    }

    private BigDecimal calculateTaxByLine(Cart cart, BigDecimal discount, BigDecimal subtotal) {
        BigDecimal tax = BigDecimal.ZERO;
        for (CartItem item : cart.getItems()) {
            BigDecimal lineDiscount = discount.multiply(item.subtotal())
                    .divide(subtotal, 12, RoundingMode.HALF_UP);
            BigDecimal taxableLine = item.subtotal().subtract(lineDiscount).max(BigDecimal.ZERO);
            tax = tax.add(taxService.calculateTax(taxableLine, item.product().category()));
        }
        return tax;
    }

    private Cart getCart(String cartId) {
        return repository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
    }

    private BigDecimal money(BigDecimal value) { return value.setScale(2, RoundingMode.HALF_UP); }
}
