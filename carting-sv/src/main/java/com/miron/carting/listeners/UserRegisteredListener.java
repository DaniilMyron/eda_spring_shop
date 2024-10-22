package com.miron.carting.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.carting.domain.Cart;
import com.miron.carting.domain.User;
import com.miron.carting.exceptions.InvalidMessageException;
import com.miron.carting.repositories.CartRepository;
import com.miron.carting.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisteredListener {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @KafkaListener(topics = "miron-user-registered", groupId = "groupId")
    public void listens(final String in) {
        log.info("Received user: {}", in);
        try {
            var userJsonObject = readStringAsJSONObject(in);
            var user = userFromPayload(userJsonObject.getJSONObject("publishedUser"));
            userRepository.save(user);
            cartRepository.save(Cart.builder()
                    .user(user)
                    .sum(0)
                    .build());
        } catch(final InvalidMessageException ex) {
            log.error("Invalid message received: {}", in);
        }
    }

    private JSONObject readStringAsJSONObject(final String json) {
        String toParse = json.substring(1, json.length() - 1);
        toParse = toParse.replace("\\", "");
        System.out.println(toParse);
        JSONObject jsonObject = new JSONObject(toParse);
        return new JSONObject(toParse);
    }

    private User userFromPayload(final JSONObject payload) {
        return User.builder()
                .id(payload.getInt("id"))
                .username(payload.getString("username"))
                .build();
    }
}
