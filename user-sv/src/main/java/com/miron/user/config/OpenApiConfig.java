package com.miron.user.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Danya Miron",
                        email = "mironn206@gmail.com",
                        url = "https://github.com/DaniilMyron"
                ),
                title = "OpenApi specification - Miron",
                description = "My demo openApi for my project",
                termsOfService = "Terms of service",
                version = "1.0",
                license = @License(
                        name = "{License name}",
                        url = "https://something.com"
                )
        )
)
public class OpenApiConfig {
}
