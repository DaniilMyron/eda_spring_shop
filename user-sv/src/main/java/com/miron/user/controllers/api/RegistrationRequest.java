package com.miron.user.controllers.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
    @NotNull(message = "100")
    @NotEmpty(message = "100")
    String username,
    @NotNull(message = "101")
    @NotEmpty(message = "101")
    String password
    ){
}
