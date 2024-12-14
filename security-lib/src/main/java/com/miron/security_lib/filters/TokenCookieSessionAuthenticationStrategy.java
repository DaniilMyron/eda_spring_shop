package com.miron.security_lib.filters;

import com.miron.security_lib.factories.DefaultTokenCookieFactory;
import com.miron.security_lib.models.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.function.Function;

@Setter
public class TokenCookieSessionAuthenticationStrategy implements SessionAuthenticationStrategy {
    private Function<Authentication, Token> tokenCookieFactory = new DefaultTokenCookieFactory();
    private Function<Token, String> tokenStringSerializer = Objects::toString;
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCookieSessionAuthenticationStrategy.class);

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        LOGGER.info(authentication.getPrincipal().toString());
        LOGGER.info("Authentication class: {}", authentication.getClass());
        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            var token = tokenCookieFactory.apply(authentication);
            var tokenString = tokenStringSerializer.apply(token);
            var cookie = new Cookie("__Host-auth-token", tokenString);
            cookie.setPath("/");
            cookie.setDomain(null);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), token.expiresAt()));

            response.addCookie(cookie);
        }
    }

}
