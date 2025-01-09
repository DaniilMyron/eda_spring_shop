package com.miron.carting.converters;

import com.miron.carting.domain.Cart;
import com.miron.carting.domain.Product;
import com.miron.carting.domain.ProductInCart;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class ObjectToProductConverter {
    public static Product productFromPayload(JSONObject payload) throws JSONException {
        return Product.builder()
                .id(payload.getInt("id"))
                .description(payload.getString("description"))
                .cost(payload.getInt("cost"))
                .name(payload.getString("name"))
                .build();
    }

    public static ProductInCart productToCartFromPayload(final JSONObject payload, int payloadCount, Cart cart) throws JSONException {
        return ProductInCart.builder()
                .id(UUID.randomUUID())
                .productId(payload.getInt("id"))
                .cart(cart)
                .count(payloadCount)
                .description(payload.getString("description"))
                .name(payload.getString("name"))
                .build();
    }
}
