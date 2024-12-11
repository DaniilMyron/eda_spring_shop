package com.miron.carting.listeners;

import com.miron.carting.converters.ObjectToProductConverter;
import com.miron.carting.domain.Cart;
import com.miron.carting.domain.Product;
import com.miron.carting.domain.ProductInCart;
import com.miron.carting.exceptions.InvalidMessageException;
import com.miron.carting.repositories.CartRepository;
import com.miron.carting.repositories.ProductInCartRepository;
import com.miron.carting.repositories.ProductRepository;
import com.miron.carting.repositories.UserRepository;
import com.miron.carting.services.impl.CartService;
import com.miron.core.converter.StringPayloadDeserializer;
import com.miron.core.converter.UsernameDeserializer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductCartedListener {
    @Autowired
    private ProductInCartRepository productInCartRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCartedListener.class);
    @Autowired
    private CartService cartService;

    @KafkaListener(topics = "miron-add-product-to-cart-event-carting", groupId = "groupId")
    public void listens(final String serializedProduct) {
        LOGGER.info("Received serialized product: {}", serializedProduct);
        try {
            var retrievedJsonObject = StringPayloadDeserializer.readStringAsJSONObject(serializedProduct);

            cartService.addProductToCart(retrievedJsonObject);
        } catch(final InvalidMessageException ex) {
            LOGGER.error("Invalid message received: {}", serializedProduct);
        }
    }
}
