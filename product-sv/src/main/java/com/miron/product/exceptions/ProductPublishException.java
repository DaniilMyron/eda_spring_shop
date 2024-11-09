package com.miron.product.exceptions;

import com.miron.product.domain.Product;
import lombok.Getter;

public class ProductPublishException extends RuntimeException{
    @Getter
    private Product product;

    public ProductPublishException(Product product) {
        this.product = product;
    }

    public ProductPublishException(String message, final Product product) {
        super(message);
        this.product = product;
    }

    public ProductPublishException(Throwable cause, final Product product) {
        super(cause);
        this.product = product;
    }

    public ProductPublishException(String message, Throwable cause, final Product product) {
        super(message, cause);
        this.product = product;
    }

    public ProductPublishException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace, final Product product) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.product = product;
    }

    public ProductPublishException(String message) {
        super(message);}
}
