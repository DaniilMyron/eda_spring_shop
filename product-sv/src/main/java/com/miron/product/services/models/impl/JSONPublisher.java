package com.miron.product.services.models.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.core.message.ProductOrderCreatedEvent;
import com.miron.core.message.ProductOrderStatusEnum;
import com.miron.core.models.PublishedProduct;
import com.miron.product.domain.Product;
import com.miron.product.exceptions.ProductPublishException;
import com.miron.product.services.models.IProductPublisher;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.slf4j.Logger;

import java.time.LocalDateTime;

public class JSONPublisher implements IProductPublisher {
    @Value("${product-sv.topic.produces.addProductToCart}")
    private String addProductToCartEvent;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONPublisher.class);

    @Override
    public void publish(Product product, int count, Object auth) {
        try {
            PublishedProduct publishedProduct = PublishedProduct.builder()
                    .id(product.getId())
                    .cost(product.getCost())
                    .count(product.getCount())
                    .description(product.getDescription())
                    .name(product.getName())
                    .authenticatedUsername(auth.toString())
                    .build();
            final var productOrderCreatedEvent =
                    new ProductOrderCreatedEvent(publishedProduct, count, LocalDateTime.now(), ProductOrderStatusEnum.CREATED);
            final String payload = objectMapper.writeValueAsString(productOrderCreatedEvent);
            LOGGER.info("Sending product order created event: {}", payload);
            kafkaTemplate.send(addProductToCartEvent, payload);
        } catch (final JsonProcessingException ex) {
            throw new ProductPublishException("Unable to publish product", ex, product);
        }
    }
}
