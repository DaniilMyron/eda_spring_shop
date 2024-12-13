package com.miron.carting.exceptions;

public class ProductNotFoundException extends CartingEntityNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException() {
        super("No such product found");
    }
}
