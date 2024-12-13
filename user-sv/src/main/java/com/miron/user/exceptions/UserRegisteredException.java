package com.miron.user.exceptions;

import com.miron.user.domain.User;
import lombok.Getter;

@Getter
public class UserRegisteredException extends RuntimeException{
    private final User user;

    public UserRegisteredException(String message, final User user) {
        super(message);
        this.user  = user;
    }

    public UserRegisteredException(String message, Throwable cause, final User user) {
        super(message, cause);
        this.user  = user;
    }
}
