package com.miron.carting.listeners;

import com.miron.carting.exceptions.InvalidMessageException;
import com.miron.carting.services.CartService;
import com.miron.core.converter.StringPayloadDeserializer;
import com.miron.core.converter.UsernameDeserializer;
import com.miron.core.message.CheckBalanceStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CheckBalanceEventResultListener {
    @Autowired
    private CartService cartService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckBalanceEventResultListener.class);

    @KafkaListener(topics = "miron-check-balance-event-result-carting", groupId = "groupId")
    public void listens(final String serializedCheckBalanceEventResult) {
        LOGGER.info("Received serialized check balance event result: {}", serializedCheckBalanceEventResult);
        try {
            var retrievedJsonObject = StringPayloadDeserializer.readStringAsJSONObject(serializedCheckBalanceEventResult);
            var payloadStatus = retrievedJsonObject.getEnum(CheckBalanceStatusEnum.class, "status");
            var payloadProductId = retrievedJsonObject.getInt("productId");

            var username = retrievedJsonObject.getString("authenticatedUsername");
            username = UsernameDeserializer.readUsernameFromPayload(username);
            if(payloadStatus == CheckBalanceStatusEnum.CANCELLED){
                LOGGER.info("Not enough money");
            } else if(payloadStatus == CheckBalanceStatusEnum.CONFIRMED){
                LOGGER.info("Enough money");
                cartService.buyFromCart(payloadProductId, username);
            }
        } catch(final InvalidMessageException ex) {
            LOGGER.error("Invalid message received: {}", serializedCheckBalanceEventResult);
        }
    }

}
