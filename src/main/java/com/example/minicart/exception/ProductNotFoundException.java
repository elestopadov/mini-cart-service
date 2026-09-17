package com.example.minicart.exception;
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) { super("Product not found in cart: " + id); }
}
