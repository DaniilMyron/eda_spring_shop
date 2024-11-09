package com.miron.carting.exceptions;

import lombok.Getter;

public class CartingPublisherException extends RuntimeException{

    public CartingPublisherException() {
    }

    public CartingPublisherException(String message) {
        super(message);
    }

    public CartingPublisherException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartingPublisherException(Throwable cause) {
        super(cause);
    }

    public CartingPublisherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
