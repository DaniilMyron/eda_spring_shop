package com.miron.carting.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.carting.controllers.CartController;
import com.miron.carting.domain.Cart;
import com.miron.carting.domain.Product;
import com.miron.carting.domain.ProductInCart;
import com.miron.carting.exceptions.InvalidMessageException;
import com.miron.carting.repositories.CartRepository;
import com.miron.carting.repositories.ProductInCartRepository;
import com.miron.carting.repositories.ProductRepository;
import com.miron.carting.repositories.UserRepository;
import com.miron.core.message.ProductOrderStatusEnum;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductPublishedListener {
    @Autowired
    private ProductInCartRepository productInCartRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartController cartController;
    @Value("${carting.topic.produces.cartingProductEvent}")
    private String cartingProductEvent;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPublishedListener.class);
    private ProductOrderStatusEnum productOrderStatusEnum;

    @KafkaListener(topics = "miron-product-carted", groupId = "groupId")
    public void listens(final String in) {
        LOGGER.info("Received Product: {}", in);
        try {
            var retrievedJsonObject = readStringAsJSONObject(in);
            var payload = retrievedJsonObject.getJSONObject("publishedProduct");
            var payloadCount = retrievedJsonObject.getInt("count");

            var username = payload.getString("authenticatedUsername");
            username = username.substring(username.indexOf('[') + 10, username.indexOf(','));
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
            LOGGER.error("Invalid message received: {}", in);
        }
    }

    private JSONObject readStringAsJSONObject(final String json) {
        String toParse = json; //json.substring(1, json.lastIndexOf(',') -1); //1 for prod - 0 for test
        toParse = toParse.replace("\\", "");
        LOGGER.info("Parsed object: {}", toParse);
        return new JSONObject(toParse);
    }

    private Product productFromPayload(JSONObject payload) {
        return Product.builder()
                .id(payload.getInt("id"))
                .count(payload.getInt("count"))
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
