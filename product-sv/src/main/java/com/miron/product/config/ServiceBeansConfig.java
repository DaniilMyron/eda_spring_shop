package com.miron.product.config;

import com.miron.product.services.models.IObjectFinder;
import com.miron.product.services.models.IProductPublisher;
import com.miron.product.services.models.impl.JSONPublisher;
import com.miron.product.services.models.impl.RequestFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ServiceBeansConfig {
    @Bean
    public IProductPublisher jsonPublisher(){
        return new JSONPublisher();
    }

    @Bean
    public IObjectFinder objectFinder(){
        return new RequestFinder();
    }
}
