package com.miron.user.converter;

import com.miron.user.controllers.api.UserResponse;
import com.miron.user.domain.User;

import java.util.function.Function;

public class UserToResponseConverter implements Function<User, UserResponse> {

    @Override
    public UserResponse apply(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .balance(user.getBalance())
                .build();
    }
}
