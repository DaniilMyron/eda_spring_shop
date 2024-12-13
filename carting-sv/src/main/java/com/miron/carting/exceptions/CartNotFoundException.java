package com.miron.carting.exceptions;

public class CartNotFoundException extends CartingEntityNotFoundException{
    public CartNotFoundException() {
        super("No such cart found");
    }
}
