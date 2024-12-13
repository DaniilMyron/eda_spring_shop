package com.miron.user.listeners;

import com.miron.core.converter.StringPayloadDeserializer;
import com.miron.core.converter.UsernameDeserializer;
import com.miron.user.exceptions.InvalidMessageException;
import com.miron.user.services.impl.ListenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CheckBalanceEventListener {
    @Autowired
    private ListenerService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckBalanceEventListener.class);

    @KafkaListener(topics = "miron-check-balance-event-user", groupId = "groupId")
    public void listens(final String serializedSum) {
        LOGGER.info("Received serialized sum: {}", serializedSum);
        try {
            var retrievedJsonObject = StringPayloadDeserializer.readStringAsJSONObject(serializedSum);
            var requiredSum = retrievedJsonObject.getInt("sum");
            var productRequestId = retrievedJsonObject.getInt("productId");
            var username = retrievedJsonObject.getString("authenticatedUsername");
            username = UsernameDeserializer.readUsernameFromPayload(username);
            userService.checkBalanceAndReserveOnBuying(username, requiredSum, productRequestId);
        } catch(final InvalidMessageException ex) {
            LOGGER.error("Invalid message received: {}", serializedSum);
        }
    }
}
