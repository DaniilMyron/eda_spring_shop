package com.miron.product.publishers.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.core.message.*;
import com.miron.core.models.PublishedProduct;
import com.miron.product.domain.Product;
import com.miron.product.exceptions.InvalidMessageException;
import com.miron.product.exceptions.ProductPublishException;
import com.miron.product.publishers.IProductEventPublisher;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ProductEventPublisher implements IProductEventPublisher {
    @Value("${product-sv.topic.produces.addProductToCartEvent}")
    private String addProductToCartEvent;
    @Value("${product-sv.topic.produces.buyingFromCartEventResult}")
    private String buyingFromCartEventValue;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventPublisher.class);

    @Override
    public void publishOrderCreatingEvent(Product product, int count, Object auth) {
        try {
            PublishedProduct publishedProduct = PublishedProduct.builder()
                    .id(product.getId())
                    .cost(product.getCost())
                    .description(product.getDescription())
                    .name(product.getName())
                    .authenticatedUsername(auth.toString())
                    .build();
            final var productOrderCreatedEvent =
                    new ProductOrderCreatedEvent(publishedProduct, count, LocalDateTime.now(), ProductOrderStatusEnum.CREATED);
            final String payload = objectMapper.writeValueAsString(productOrderCreatedEvent);
            LOGGER.info("Sending product order created event: {}", productOrderCreatedEvent);
            kafkaTemplate.send(addProductToCartEvent, payload);
        } catch (final JsonProcessingException ex) {
            throw new ProductPublishException("Unable to publish product", ex, product);
        }
    }

    @Override
    public void publishBuyingFromCartEventResult(List<Product> changedProductsCount, boolean isConfirmed, String username, Map<Integer, Integer> productsCount) {
        try {
            BuyingFromCartEventResult buyingFromCartEventResult;
            if(isConfirmed) {
                buyingFromCartEventResult = new BuyingFromCartEventResult(username, null, LocalDateTime.now(), BuyingFromCartStatusEnum.CONFIRMED, productsCount);
            } else {
                buyingFromCartEventResult = new BuyingFromCartEventResult(username, changedProductsCount, LocalDateTime.now(), BuyingFromCartStatusEnum.CANCELLED, productsCount);
            }
            final String payload = objectMapper.writeValueAsString(buyingFromCartEventResult);
            LOGGER.info("Sending buying from cart event result: {}", buyingFromCartEventResult);
            kafkaTemplate.send(buyingFromCartEventValue, payload);
        } catch (final JsonProcessingException ex) {
            throw new InvalidMessageException("Unable to publish event", ex);
        }
    }
}
