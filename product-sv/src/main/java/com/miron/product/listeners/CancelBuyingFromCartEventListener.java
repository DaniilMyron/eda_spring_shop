package com.miron.product.listeners;

import com.miron.core.converter.StringPayloadDeserializer;
import com.miron.product.exceptions.InvalidMessageException;
import com.miron.product.services.impl.ListenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CancelBuyingFromCartEventListener {
    @Autowired
    private ListenerService listenerService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelBuyingFromCartEventListener.class);

    @KafkaListener(topics = "miron-cancel-buying-from-cart-event-carting", groupId = "groupId")
    public void listens(final String serializedCancelledProductsInCart) {
        LOGGER.info("Received serialized cancelled products in cart: {}", serializedCancelledProductsInCart);
        try {
            var retrievedJsonObject = StringPayloadDeserializer.readStringAsJSONObject(serializedCancelledProductsInCart);
            var cancelledProductsInCart = retrievedJsonObject.getJSONObject("json");

            listenerService.returnCancelledProductsCount(cancelledProductsInCart);
        } catch(final InvalidMessageException ex) {
            LOGGER.error("Invalid message received: {}", serializedCancelledProductsInCart);
        }
    }
}
