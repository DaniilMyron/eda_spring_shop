package com.miron.carting.exceptions;

import com.miron.security_lib.handler.ServiceEntityNotFoundException;

public class ProductInCartNotFoundException extends ServiceEntityNotFoundException {
    public ProductInCartNotFoundException(String message) {
        super(message);
    }
    public ProductInCartNotFoundException() {
        super("No such product in cart found");
    }
}
