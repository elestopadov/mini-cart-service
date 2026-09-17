package com.example.minicart.exception;
public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String id) { super("Cart not found: " + id); }
}
