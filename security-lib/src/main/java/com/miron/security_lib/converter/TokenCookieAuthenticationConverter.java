package com.miron.security_lib.converter;

import com.miron.security_lib.models.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.function.Function;
import java.util.stream.Stream;

public class TokenCookieAuthenticationConverter implements AuthenticationConverter {
    private final Function<String, Token> tokenCookieStringDeserializer;

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
