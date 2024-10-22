package com.miron.user.services.models.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.core.message.UserRegisteredEvent;
import com.miron.core.models.PublishedUser;
import com.miron.user.domain.User;
import com.miron.user.exceptions.UserRegisteredException;
import org.springframework.kafka.core.KafkaTemplate;
import com.miron.user.services.models.IUserJsonPublisher;

import java.time.LocalDateTime;

public class UserJsonPublisher implements IUserJsonPublisher {
    private final String topicName;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public UserJsonPublisher(KafkaTemplate<String, Object> kafkaTemplate, String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void publish(User user) {
        try {
            PublishedUser publishedUser = PublishedUser.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .build();
            final var userRegisteredEvent =
                    new UserRegisteredEvent(publishedUser, LocalDateTime.now());
            final String payload = objectMapper.writeValueAsString(userRegisteredEvent);
            kafkaTemplate.send(topicName, payload);
        } catch (final JsonProcessingException ex) {
            throw new UserRegisteredException("Unable to publish product", ex, user);
        }
    }
}
