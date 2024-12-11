package com.miron.carting.converters;

import com.miron.carting.domain.User;
import org.json.JSONObject;

public class ObjectToUserConverter {
    public static User userFromPayload(final JSONObject payload) {
        return User.builder()
                .id(payload.getInt("id"))
                .username(payload.getString("username"))
                .build();
    }
}
