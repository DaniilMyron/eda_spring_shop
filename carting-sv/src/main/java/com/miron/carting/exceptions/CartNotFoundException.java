package com.miron.carting.exceptions;

import com.miron.security_lib.handler.ServiceEntityNotFoundException;

public class CartNotFoundException extends ServiceEntityNotFoundException {
    public CartNotFoundException() {
        super("No such cart found");
    }
}
