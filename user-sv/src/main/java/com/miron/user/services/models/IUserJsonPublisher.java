package com.miron.user.services.models;

import com.miron.user.domain.User;

public interface IUserJsonPublisher {
    void publish(User user);
}
