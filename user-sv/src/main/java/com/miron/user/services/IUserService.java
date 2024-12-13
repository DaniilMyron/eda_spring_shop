package com.miron.user.services;

import com.miron.user.controllers.api.RegistrationRequest;
import com.miron.user.controllers.api.UserResponse;
import org.springframework.security.core.Authentication;

public interface IUserService {
    UserResponse registerUser(RegistrationRequest request);
    void authenticateUser();

    UserResponse replenishBalance(Authentication authentication, int sum);
}
