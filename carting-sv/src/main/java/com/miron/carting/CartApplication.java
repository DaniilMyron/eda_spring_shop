package com.miron.carting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.web.SecurityFilterChain;


@SpringBootApplication
@ComponentScan(basePackages = {"com.miron.*"})
public class CartApplication {
    @Autowired
    private SecurityFilterChain webSecurityConfig;
    public static void main(String[] args){
        SpringApplication.run(CartApplication.class, args);
    }
}
