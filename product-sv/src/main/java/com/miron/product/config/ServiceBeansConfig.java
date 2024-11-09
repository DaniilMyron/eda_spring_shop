package com.miron.product.config;

import com.miron.product.publishers.IProductEventPublisher;
import com.miron.product.publishers.impl.ProductEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBeansConfig {
    @Bean
    public IProductEventPublisher jsonPublisher(){
        return new ProductEventPublisher();
    }
}
