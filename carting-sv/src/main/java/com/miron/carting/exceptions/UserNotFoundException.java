package com.miron.carting.exceptions;

import com.miron.security_lib.handler.ServiceEntityNotFoundException;

public class UserNotFoundException extends ServiceEntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException() {
        super("No such user found");
    }
}
