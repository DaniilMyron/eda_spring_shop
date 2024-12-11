package com.miron.carting.publishers.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.carting.domain.ProductInCart;
import com.miron.carting.exceptions.CartingPublisherException;
import com.miron.carting.publishers.ICartingEventPublisher;
import com.miron.core.message.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CartingEventPublisher implements ICartingEventPublisher {
    @Value("${carting-sv.topic.produces.checkBalanceEvent}")
    private String checkBalanceEventValue;
    @Value("${carting-sv.topic.produces.buyingFromCartEvent}")
    private String buyingFromCartEventValue;
    @Value("${carting-sv.topic.produces.cancelBuyingFromCartEvent}")
    private String cancelBuyingFromCartEventValue;
    @Value("${carting-sv.topic.produces.changeBalanceEvent}")
    private String changeBalanceEventValue;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Logger LOGGER = LoggerFactory.getLogger(CartingEventPublisher.class);

    @Override
    public void publishBalanceEvent(int productId, Authentication authentication, int sum) {
        try {
            EventMessage checkBalanceCreatedEvent = new CheckBalanceEvent(
                    productId,
                    authentication.toString(),
                    sum,
                    LocalDateTime.now(),
                    CheckBalanceStatusEnum.CREATED
            );
            final String payload = objectMapper.writeValueAsString(checkBalanceCreatedEvent);
            LOGGER.info("Sending product order created event: {}", checkBalanceCreatedEvent);
            kafkaTemplate.send(checkBalanceEventValue, payload);
        } catch (final JsonProcessingException ex) {
            throw new CartingPublisherException("Unable to publish check balance event", ex);
        }
    }

    @Override
    public void publishBuyingEvent(List<ProductInCart> productInCart, String username) {
        try {
            EventMessage buyingFromCartEvent = new BuyingFromCartEvent(
                    username,
                    productInCart,
                    LocalDateTime.now(),
                    BuyingFromCartStatusEnum.CREATED
            );
            final String payload = objectMapper.writeValueAsString(buyingFromCartEvent);
            LOGGER.info("Sending buying from cart event created: {}", buyingFromCartEvent);
            kafkaTemplate.send(buyingFromCartEventValue, payload);
        } catch (final JsonProcessingException ex) {
            throw new CartingPublisherException("Unable to publish buying from cart event", ex);
        }
    }

    @Override
    public void publishCancelBuyingEvent(Map<Integer, Integer> canceledProductsCount) {
        try {
            EventMessage cancelBuyingFromCartEvent = new CancelBuyingFromCartEvent(
                    canceledProductsCount,
                    LocalDateTime.now()
            );
            final String payload = objectMapper.writeValueAsString(cancelBuyingFromCartEvent);
            LOGGER.info("Sending cancelled buying products: {}", cancelBuyingFromCartEvent);
            kafkaTemplate.send(cancelBuyingFromCartEventValue, payload);
        } catch (final JsonProcessingException ex) {
            throw new CartingPublisherException("Unable to publish cancelled buying from cart event", ex);
        }
    }

    @Override
    public void publishChangeBalanceEvent(String username, ChangeBalanceStatusEnum changeBalanceStatusEnum, Map<Integer, Integer> productsCountOnId) {
        try {
            EventMessage changeBalanceEvent = new ChangeBalanceEvent(
                    username,
                    productsCountOnId,
                    LocalDateTime.now(),
                    changeBalanceStatusEnum
            );
            final String payload = objectMapper.writeValueAsString(changeBalanceEvent);
            LOGGER.info("Sending change balance event: {}", changeBalanceEvent);
            kafkaTemplate.send(changeBalanceEventValue, payload);
        } catch (final JsonProcessingException ex) {
            throw new CartingPublisherException("Unable to publish change balance event", ex);
        }
    }
}
