package com.miron.user.publishers.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.core.message.*;
import com.miron.core.models.ProductsInCartToReturn;
import com.miron.core.models.PublishedUser;
import com.miron.core.models.UserInfoForCheck;
import com.miron.user.domain.User;
import com.miron.user.exceptions.InvalidMessageException;
import com.miron.user.exceptions.UserRegisteredException;
import com.miron.user.publishers.IUserEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

public class UserEventPublisher implements IUserEventPublisher {
    @Value("${user-sv.topic.produces.userRegisteredEvent}")
    private String userRegisteredEventTopic;
    @Value("${user-sv.topic.produces.checkBalanceEvent}")
    private String checkBalanceEventTopic;
    @Value("${user-sv.topic.produces.sendUserInfoForCheckEvent}")
    private String sendUserInfoEventValue;
    @Value("${user-sv.topic.produces.returnProductsInCartEventValue}")
    private String returnProductsInCartEventValue;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventPublisher.class);
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void publish(User user) {
        try {
            PublishedUser publishedUser = PublishedUser.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .build();
            EventMessage userRegisteredEvent = new UserRegisteredEvent(
                    publishedUser,
                    LocalDateTime.now()
            );
            final String payload = objectMapper.writeValueAsString(userRegisteredEvent);
            LOGGER.info("Sending user registration event: {}", userRegisteredEvent);
            kafkaTemplate.send(userRegisteredEventTopic, payload);
        } catch (final JsonProcessingException ex) {
            throw new UserRegisteredException("Unable to publish product", ex, user);
        }
    }

    @Override
    public void publishCheckBalanceEvent(String username, int requiredSum, int productRequiredId, CheckBalanceStatusEnum status) {
        try {
            EventMessage checkBalanceCreatedEvent = new CheckBalanceEvent(
                            productRequiredId,
                            username,
                            requiredSum,
                            LocalDateTime.now(),
                            status
            );
            final String payload = objectMapper.writeValueAsString(checkBalanceCreatedEvent);
            LOGGER.info("Sending result of check balance event: {}", checkBalanceCreatedEvent);
            kafkaTemplate.send(checkBalanceEventTopic, payload);
        } catch (final JsonProcessingException ex) {
            throw new InvalidMessageException("Unable to publish check balance event", ex);
        }
    }

    @Override
    public void publishUserInfoForCheck(UserInfoForCheck userInfoForCheck) {
        try {
            EventMessage sendUserInfoEvent = new SendUserInfoEvent(
                    userInfoForCheck,
                    LocalDateTime.now(),
                    SendUserInfoStatusEnum.CREATED
            );
            final String payload = objectMapper.writeValueAsString(sendUserInfoEvent);
            LOGGER.info("Sending user info event: {}", sendUserInfoEvent);
            kafkaTemplate.send(sendUserInfoEventValue, payload);
        } catch (final JsonProcessingException ex) {
            throw new InvalidMessageException("Unable to publish user info event", ex);
        }
    }

    @Override
    public void publishGetBackProductsInCart(ProductsInCartToReturn productsInCartToReturn) {
        try {
            EventMessage returnProductsInCart = new ReturnProductsInCart(
                    productsInCartToReturn,
                    LocalDateTime.now(),
                    ReturnProductsInCartStatusEnum.CREATED
            );
            final String payload = objectMapper.writeValueAsString(returnProductsInCart);
            LOGGER.info("Sending products in cart to be returned event: {}", returnProductsInCart);
            kafkaTemplate.send(returnProductsInCartEventValue, payload);
        } catch (final JsonProcessingException ex) {
            throw new InvalidMessageException("Unable to publish user info event", ex);
        }
    }
}
