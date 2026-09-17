package com.example.minicart.exception;
public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String id) { super("Cart is empty: " + id); }
}
