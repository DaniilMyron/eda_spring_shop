package com.miron.user.services;

import com.miron.user.controllers.api.RegistrationRequest;
import com.miron.user.domain.User;
import com.miron.user.services.models.IUserJsonPublisher;

public interface IUserService {
    void setPublisher(IUserJsonPublisher userJsonPublisher);
    void registerUser(RegistrationRequest request);
    void authenticateUser();

    User getAuthenticatedUser(Object authentication, int sum);
}
