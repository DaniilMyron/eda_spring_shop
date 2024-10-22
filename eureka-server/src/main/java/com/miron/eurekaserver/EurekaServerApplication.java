package com.miron.eurekaserver;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(EurekaServerApplication.class);
        springApplication.run(args);
    }
}
