package com.miron.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.miron.*"})
public class UsersApplication {
    public static void main(String[] args){
        SpringApplication.run(UsersApplication.class, args);
    }
}
