package com.miron.core.message;

import java.time.LocalDateTime;
import java.util.List;

public class BuyingFromCartEvent extends BuyingFromCart{

    public BuyingFromCartEvent(String authenticatedUsername, List<?> productsInCart, LocalDateTime timestamp, BuyingFromCartStatusEnum status) {
        super(authenticatedUsername, productsInCart, timestamp, status);
    }
}
