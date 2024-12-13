package com.miron.product.config;

import com.miron.product.publishers.IProductEventPublisher;
import com.miron.product.publishers.impl.ProductEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class ProductServiceConfig {
    @Bean
    public IProductEventPublisher productEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        return new ProductEventPublisher(kafkaTemplate);
    }
}
