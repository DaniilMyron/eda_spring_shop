package com.miron.core.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsernameDeserializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsernameDeserializer.class);

    public static String readUsernameFromPayload(final String jsonUsername) {
        LOGGER.info("Incoming username: {}", jsonUsername);
        String username;
        if(jsonUsername.contains(",")){
            username = jsonUsername.substring(jsonUsername.indexOf('[') + 10, jsonUsername.indexOf(','));
        } else{
            username = jsonUsername;
        }
        LOGGER.info("Parsed username: {}", username);
        return username;
    }
}
