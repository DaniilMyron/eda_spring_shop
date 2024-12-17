package com.miron.user.listeners;

import com.miron.core.converter.StringPayloadDeserializer;
import com.miron.core.converter.UsernameDeserializer;
import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.user.exceptions.InvalidMessageException;
import com.miron.user.services.impl.ListenerService;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ChangeBalanceEventListener {
    @Autowired
    private ListenerService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeBalanceEventListener.class);

    @KafkaListener(topics = "miron-change-balance-event-user", groupId = "groupId")
    public void listens(final String serializedChangeBalance) {
        LOGGER.info("Received serialized change balance event: {}", serializedChangeBalance);
        try {
            var retrievedJsonObject = StringPayloadDeserializer.readStringAsJSONObject(serializedChangeBalance);
            var payloadStatus = retrievedJsonObject.getEnum(ChangeBalanceStatusEnum.class, "status");
            var username = retrievedJsonObject.getString("authenticatedUsername");
            var productsCountOnId = retrievedJsonObject.getJSONObject("productsCountOnId");
            username = UsernameDeserializer.readUsernameFromPayload(username);
            userService.changeBalanceAndMakeCheck(username, payloadStatus, productsCountOnId);
        } catch(final InvalidMessageException | JSONException ex) {
            LOGGER.error("Invalid message received: {}", serializedChangeBalance);
        }
    }
}
