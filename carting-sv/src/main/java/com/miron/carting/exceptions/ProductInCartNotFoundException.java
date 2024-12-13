package com.miron.carting.exceptions;

public class ProductInCartNotFoundException extends CartingEntityNotFoundException {
    public ProductInCartNotFoundException(String message) {
        super(message);
    }
    public ProductInCartNotFoundException() {
        super("No such product in cart found");
    }
}
