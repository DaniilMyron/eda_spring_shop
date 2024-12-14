package com.miron.security_lib.configurer;

import com.miron.security_lib.converter.TokenCookieAuthenticationConverter;
import com.miron.security_lib.filters.TokenCookieSessionAuthenticationStrategy;
import com.miron.security_lib.models.DeactivatedToken;
import com.miron.security_lib.models.Token;
import com.miron.security_lib.models.TokenAuthenticationUserDetailsService;
import com.miron.security_lib.models.TokenUser;
import com.miron.security_lib.repo.DeactivatedTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;

import java.util.Date;
import java.util.function.Function;

public class TokenCookieAuthenticationConfigurer
        extends AbstractHttpConfigurer<TokenCookieAuthenticationConfigurer, HttpSecurity> {
    private Function<String, Token> tokenCookieStringDeserializer;
    @Autowired
    private DeactivatedTokenRepository deactivatedTokenRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCookieAuthenticationConfigurer.class);

    @Override
    public void init(HttpSecurity builder) throws Exception {
        builder.logout(logout -> logout.addLogoutHandler(
                new CookieClearingLogoutHandler("__Host-auth-token"))
                .addLogoutHandler((request, response, authentication) -> {
                    if(authentication != null && authentication.getPrincipal() instanceof TokenUser tokenUser){
                        var deactivatedToken = new DeactivatedToken(tokenUser.getToken().id(), Date.from(tokenUser.getToken().expiresAt()));
                        deactivatedTokenRepository.save(deactivatedToken);

                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }));
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var cookieAuthenticationFilter = new AuthenticationFilter(
                builder.getSharedObject(AuthenticationManager.class),
                new TokenCookieAuthenticationConverter(this.tokenCookieStringDeserializer));

        cookieAuthenticationFilter.setSuccessHandler((request, response, authentication) -> {});
        cookieAuthenticationFilter.setFailureHandler(new AuthenticationEntryPointFailureHandler(new Http403ForbiddenEntryPoint()));

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(new TokenAuthenticationUserDetailsService());

        builder.addFilterAfter(cookieAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(authenticationProvider);
    }

    public TokenCookieAuthenticationConfigurer tokenCookieStringDeserializer(Function<String, Token> tokenCookieStringDeserializer) {
        this.tokenCookieStringDeserializer = tokenCookieStringDeserializer;
        return this;
    }
}
