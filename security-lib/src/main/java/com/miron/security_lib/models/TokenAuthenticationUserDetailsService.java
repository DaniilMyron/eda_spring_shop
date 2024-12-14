package com.miron.security_lib.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationUserDetailsService.class);

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if(authenticationToken.getPrincipal() instanceof Token token) {
            return new TokenUser(token.subject(),
                    "nopassword",
                    true,
                    true,
                    token.expiresAt().isAfter(Instant.now()),
                    true,
                    token.authorities().stream()
                        .map(authority -> new SimpleGrantedAuthority("USER_ROLE"))
                        .toList(),
                    token);
        }
        throw new UsernameNotFoundException("Principal must be of type Token");
    }
}
