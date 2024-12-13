package com.miron.carting.listeners;

import com.miron.carting.exceptions.InvalidMessageException;
import com.miron.carting.services.impl.CartService;
import com.miron.core.converter.ObjectToMapConverter;
import com.miron.core.converter.StringPayloadDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReturnProductsInCartEventListener {
    @Autowired
    private CartService cartService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReturnProductsInCartEventListener.class);


    @KafkaListener(topics = "miron-return-products-in-cart-event-carting", groupId = "groupId")
    public void listens(final String serializedReturnProductsInCartEvent) {
        LOGGER.info("Received serialized return products in cart event: {}", serializedReturnProductsInCartEvent);
        try {
            var retrievedJsonObject = StringPayloadDeserializer.readStringAsJSONObject(serializedReturnProductsInCartEvent);
            var productsCountOnId = ObjectToMapConverter.convertJSONObjectToMap(retrievedJsonObject.getJSONObject("productsCountOnId"));

            cartService.returnProductsInCart(productsCountOnId);
        } catch(final InvalidMessageException ex) {
            LOGGER.error("Invalid message received: {}", serializedReturnProductsInCartEvent);
        }
    }
}
