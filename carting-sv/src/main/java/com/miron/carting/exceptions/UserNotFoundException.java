package com.miron.carting.exceptions;

public class UserNotFoundException extends CartingEntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException() {
        super("No such user found");
    }
}
