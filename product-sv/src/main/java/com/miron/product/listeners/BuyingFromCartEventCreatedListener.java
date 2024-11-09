package com.miron.product.listeners;

import com.miron.core.converter.StringPayloadDeserializer;
import com.miron.core.converter.UsernameDeserializer;
import com.miron.product.exceptions.InvalidMessageException;
import com.miron.product.services.impl.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class BuyingFromCartEventCreatedListener {
    @Autowired
    private ProductService productService;
    private static final Logger LOGGER = LoggerFactory.getLogger(BuyingFromCartEventCreatedListener.class);

    @KafkaListener(topics = "miron-buying-from-cart-event-product", groupId = "groupId")
    public void listens(final String serializedProductsInCart) {
        LOGGER.info("Received serialized products in cart: {}", serializedProductsInCart);
        try {
            var retrievedJsonObject = StringPayloadDeserializer.readStringAsJSONObject(serializedProductsInCart);
            var productsInCartArray = retrievedJsonObject.getJSONArray("productsInCart");
            var username = retrievedJsonObject.getString("authenticatedUsername");
            username = UsernameDeserializer.readUsernameFromPayload(username);

            productService.isCountValid(productsInCartArray, username);
        } catch(final InvalidMessageException ex) {
            LOGGER.error("Invalid message received: {}", serializedProductsInCart);
        }
    }
}