package com.miron.carting.config;

import com.miron.carting.publishers.ICartingEventPublisher;
import com.miron.carting.publishers.impl.CartingEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class CartingServiceConfig {
    @Bean
    public ICartingEventPublisher cartingEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        return new CartingEventPublisher(kafkaTemplate);
    }
}
