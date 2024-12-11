package com.miron.core.converter;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringPayloadDeserializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(StringPayloadDeserializer.class);

    public static JSONObject readStringAsJSONObject(final String json) throws JSONException {
        LOGGER.info("Incoming object: {}", json);
        String toParse = json.substring(1, json.length() - 1); //1 for prod - 0 for test
        toParse = toParse.replace("\\", "");
        LOGGER.info("Parsed object: {}", toParse);
        return new JSONObject(toParse);
    }
}
