package com.miron.carting.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.carting.domain.Cart;
import com.miron.carting.domain.User;
import com.miron.carting.exceptions.InvalidMessageException;
import com.miron.carting.repositories.CartRepository;
import com.miron.carting.repositories.UserRepository;
import com.miron.carting.services.impl.CartService;
import com.miron.core.converter.StringPayloadDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisteredEventListener {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private CartService cartService;

    @KafkaListener(topics = "miron-user-registered", groupId = "groupId")
    public void listens(final String in) {
        log.info("Received user: {}", in);
        try {
            var userJsonObject = StringPayloadDeserializer.readStringAsJSONObject(in);
            cartService.createCartOnUser(userJsonObject);
        } catch(final InvalidMessageException ex) {
            log.error("Invalid message received: {}", in);
        }
    }
}
