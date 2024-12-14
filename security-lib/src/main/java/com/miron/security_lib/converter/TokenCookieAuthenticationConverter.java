package com.miron.security_lib.converter;

import com.miron.security_lib.filters.TokenCookieSessionAuthenticationStrategy;
import com.miron.security_lib.models.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

public class TokenCookieAuthenticationConverter implements AuthenticationConverter {
    private final Function<String, Token> tokenCookieStringDeserializer;
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCookieAuthenticationConverter.class);

    public TokenCookieAuthenticationConverter(Function<String, Token> tokenCookieStringDeserializer) {
        this.tokenCookieStringDeserializer = tokenCookieStringDeserializer;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        if(request.getCookies() != null){
            return Stream.of(request.getCookies())
                    .filter(cookie -> cookie.getName().startsWith("__Host-auth-token"))
                    .findFirst()
                    .map(cookie -> {
                        var token = tokenCookieStringDeserializer.apply(cookie.getValue());
                        return new PreAuthenticatedAuthenticationToken(token, cookie.getValue());
                    })
                    .orElse(null);
        }
        return null;
    }
}
