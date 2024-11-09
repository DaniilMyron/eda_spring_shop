package com.miron.carting.listeners;

import com.miron.carting.domain.Cart;
import com.miron.carting.domain.Product;
import com.miron.carting.domain.ProductInCart;
import com.miron.carting.exceptions.InvalidMessageException;
import com.miron.carting.repositories.CartRepository;
import com.miron.carting.repositories.ProductInCartRepository;
import com.miron.carting.repositories.ProductRepository;
import com.miron.carting.repositories.UserRepository;
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

    @KafkaListener(topics = "miron-product-carted", groupId = "groupId")
    public void listens(final String serializedProduct) {
        LOGGER.info("Received serialized product: {}", serializedProduct);
        try {
            var retrievedJsonObject = StringPayloadDeserializer.readStringAsJSONObject(serializedProduct);
            var payload = retrievedJsonObject.getJSONObject("publishedProduct");
            var payloadCount = retrievedJsonObject.getInt("count");

            var username = payload.getString("authenticatedUsername");
            username = UsernameDeserializer.readUsernameFromPayload(username);
            var user = userRepository.findByUsername(username);
            var cart = cartRepository.findByUserId(user.getId());

            var productToCart = productToCartFromPayload(payload, payloadCount, cart);
            var product = productFromPayload(payload);

            var foundedProduct = productInCartRepository.findByProductId(productToCart.getProductId());
            if(foundedProduct != null){
                foundedProduct.setCount(payloadCount + foundedProduct.getCount());
                productInCartRepository.save(foundedProduct);
            } else {
                productInCartRepository.save(productToCart);
            }
            productRepository.save(product);

            cartRepository.save(Cart.builder()
                    .id(cart.getId())
                    .sum(cart.getSum() + payloadCount * payload.getInt("cost"))
                    .user(cart.getUser())
                    .build());

            LOGGER.info("Product saved to cart: {}", productToCart);
        } catch(final InvalidMessageException ex) {
            LOGGER.error("Invalid message received: {}", serializedProduct);
        }
    }

    private Product productFromPayload(JSONObject payload) {
        return Product.builder()
                .id(payload.getInt("id"))
                .description(payload.getString("description"))
                .cost(payload.getInt("cost"))
                .name(payload.getString("name"))
                .build();
    }

    private ProductInCart productToCartFromPayload(final JSONObject payload, int payloadCount, Cart cart) {
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
