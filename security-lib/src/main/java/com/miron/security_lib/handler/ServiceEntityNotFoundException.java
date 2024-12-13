package com.miron.security_lib.handler;

public class ServiceEntityNotFoundException extends RuntimeException {
    public ServiceEntityNotFoundException(String message) {
        super(message);
    }
}
