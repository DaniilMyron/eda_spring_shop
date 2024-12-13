package com.miron.carting.exceptions;

public class CartingEntityNotFoundException extends RuntimeException {
    public CartingEntityNotFoundException(String message) {
        super(message);
    }
}
