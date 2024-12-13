package com.miron.product.exceptions;

import com.miron.security_lib.handler.ServiceEntityNotFoundException;

public class ProductNotFoundException extends ServiceEntityNotFoundException {
    public ProductNotFoundException() {
        super("No such product found");
    }
}
