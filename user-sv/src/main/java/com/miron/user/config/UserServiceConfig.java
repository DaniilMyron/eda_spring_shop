package com.miron.user.config;

import com.miron.user.publishers.IUserEventPublisher;
import com.miron.user.publishers.impl.UserEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class UserServiceConfig {
    @Bean
    public IUserEventPublisher userEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        return new UserEventPublisher(kafkaTemplate);
    }
}
