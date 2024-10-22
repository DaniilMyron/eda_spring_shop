package com.miron.security_lib;

import com.miron.security_lib.configurer.TokenCookieAuthenticationConfigurer;
import com.miron.security_lib.deserializers.TokenCookieJweStringDeserializer;
import com.miron.security_lib.filters.GetCsrfTokenFilter;
import com.miron.security_lib.filters.TokenCookieSessionAuthenticationStrategy;
import com.miron.security_lib.serializers.TokenCookieJweStringSerializer;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
public class WebSecurityConfig {
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception{
//        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
//        authenticationProvider.setPreAuthenticatedUserDetailsService(new TokenAuthenticationUserDetailsService());
//
//        authBuilder.userDetailsService(userDetailsService());
//        authBuilder.authenticationProvider(authenticationProvider);
//    }
//    @Bean
//    public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
//            @Value("${jwt.access-token-key}") String accessTokenKey,
//            @Value("${jwt.refresh-token-key}") String refreshTokenKey
//    ) throws ParseException, JOSEException {
//        return new JwtAuthenticationConfigurer()
//                .accessTokenStringSerializer(new AccessTokenJwsStringSerializer(
//                        new MACSigner(OctetSequenceKey.parse(accessTokenKey))))
//                .refreshTokenStringSerializer(new RefreshTokenJweStringSerializer(
//                        new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey))
//                ))
//                .accessTokenStringDeserializer(new AccessTokenJwsStringDeserializer(
//                        new MACVerifier(OctetSequenceKey.parse(accessTokenKey))
//                ))
//                .refreshTokenStringDeserializer(new RefreshTokenJweStringDeserializer(
//                        new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey))
//                ));
//    }
    @Bean
    public TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
            @Value("${jwt.cookie-token-key}") String cookieTokenKey
    ) throws Exception{
        return new TokenCookieAuthenticationConfigurer()
                .tokenCookieStringDeserializer(new TokenCookieJweStringDeserializer(
                        new DirectDecrypter(OctetSequenceKey.parse(cookieTokenKey))
                ));
    }

    @Bean
    public TokenCookieJweStringSerializer tokenCookieJweStringSerializer(
            @Value("${jwt.cookie-token-key}") String cookieTokenKey
    )throws Exception {
        return new TokenCookieJweStringSerializer(new DirectEncrypter(OctetSequenceKey.parse(cookieTokenKey)));
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity https,
            TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer,
            TokenCookieJweStringSerializer tokenCookieJweStringSerializer
    ) throws Exception {
        var tokenCookieSessionAuthenticationStrategy = new TokenCookieSessionAuthenticationStrategy();
        tokenCookieSessionAuthenticationStrategy.setTokenStringSerializer(tokenCookieJweStringSerializer);

        https
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionAuthenticationStrategy(tokenCookieSessionAuthenticationStrategy))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/products/hello",
                                "/api/v1/products/hello2",
                                "/api/v1/users/register")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .sessionAuthenticationStrategy((authentication, request, response) -> {}));

        https.with(tokenCookieAuthenticationConfigurer, Customizer.withDefaults());
        return https.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
