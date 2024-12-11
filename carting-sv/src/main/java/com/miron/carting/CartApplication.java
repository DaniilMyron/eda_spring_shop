package com.miron.carting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@ComponentScan(basePackages = {"com.miron.*"})
@EnableJpaAuditing
public class CartApplication {
    public static void main(String[] args){
        SpringApplication.run(CartApplication.class, args);
    }
}
