package com.miron.core.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public abstract class BuyingFromCart implements EventMessage{
    private String authenticatedUsername;
    private List<?> productsInCart;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime timestamp;
    private BuyingFromCartStatusEnum status;

    public BuyingFromCart() {
    }

    public BuyingFromCart(String authenticatedUsername, List<?> productsInCart, LocalDateTime timestamp, BuyingFromCartStatusEnum status) {
        this.authenticatedUsername = authenticatedUsername;
        this.productsInCart = productsInCart;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

}
