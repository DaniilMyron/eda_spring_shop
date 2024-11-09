package com.miron.carting.listeners;

import com.miron.carting.exceptions.InvalidMessageException;
import com.miron.carting.services.impl.CartService;
import com.miron.core.converter.StringPayloadDeserializer;
import com.miron.core.models.UserInfoForCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SendUserInfoEventListener {
    @Autowired
    private CartService cartService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SendUserInfoEventListener.class);

    @KafkaListener(topics = "miron-send-user-info-event-carting", groupId = "groupId")
    public void listens(final String serializedSendUserInfoEvent) {
        LOGGER.info("Received serialized send user info event: {}", serializedSendUserInfoEvent);
        try {
            var retrievedJsonObject = StringPayloadDeserializer.readStringAsJSONObject(serializedSendUserInfoEvent);

            var payloadUserInfo = retrievedJsonObject.getJSONObject("userInfo");
            var userInfo = UserInfoForCheck.builder()
                    .payingSum(payloadUserInfo.getInt("payingSum"))
                    .authenticatedUsername(payloadUserInfo.getString("authenticatedUsername"))
                    .build();
            cartService.makeCheck(userInfo);
            cartService.clearCart(userInfo);
        } catch(final InvalidMessageException ex) {
            LOGGER.error("Invalid message received: {}", serializedSendUserInfoEvent);
        }
    }
}
