package com.miron.user.exceptions;

import com.miron.security_lib.handler.ServiceEntityNotFoundException;

public class UserNotFoundException extends ServiceEntityNotFoundException {
    public UserNotFoundException() {
        super("No such user found");
    }
}
