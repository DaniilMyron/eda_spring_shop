package com.miron.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailsServiceConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        return new JpaUserDetailsService();
    }
}
