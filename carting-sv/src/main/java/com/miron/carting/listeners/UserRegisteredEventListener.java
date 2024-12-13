package com.miron.carting.listeners;

import com.miron.carting.exceptions.InvalidMessageException;
import com.miron.carting.services.impl.ListenerService;
import com.miron.core.converter.StringPayloadDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisteredEventListener {
    @Autowired
    private ListenerService listenerService;

    @KafkaListener(topics = "miron-user-registered-event-carting", groupId = "groupId")
    public void listens(final String in) {
        log.info("Received user: {}", in);
        try {
            var userJsonObject = StringPayloadDeserializer.readStringAsJSONObject(in);
            listenerService.createCartOnUser(userJsonObject);
        } catch(final InvalidMessageException ex) {
            log.error("Invalid message received: {}", in);
        }
    }
}
