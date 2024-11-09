package com.miron.core.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class BuyingFromCartEventResult extends BuyingFromCart{
    private Map<Integer, Integer> count;

    public BuyingFromCartEventResult() {
    }

    public BuyingFromCartEventResult(String authenticatedUsername, List<?> productsInCart, LocalDateTime timestamp, BuyingFromCartStatusEnum status, Map<Integer, Integer> count) {
        super(authenticatedUsername, productsInCart, timestamp, status);
        this.count = count;
    }
}
