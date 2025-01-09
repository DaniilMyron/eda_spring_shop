package com.miron.carting.converters;

import com.miron.carting.domain.User;
import org.json.JSONException;
import org.json.JSONObject;

public class ObjectToUserConverter {
    public static User userFromPayload(final JSONObject payload) {
        try {
            return User.builder()
                    .id(payload.getInt("id"))
                    .username(payload.getString("username"))
                    .build();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
