package com.example.minicart.exception;
public class InvalidPromoCodeException extends RuntimeException {
    public InvalidPromoCodeException(String code) { super("Promo code is not applicable: " + code); }
}
