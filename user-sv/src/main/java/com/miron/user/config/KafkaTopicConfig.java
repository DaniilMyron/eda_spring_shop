package com.miron.user.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${user-sv.topic.produces.userRegisteredEvent}")
    private String userRegisteredEvent;
    @Value("${user-sv.topic.produces.checkBalanceEvent}")
    private String checkBalanceEvent;

    @Bean
    public NewTopic userRegisteredEventTopic(){
        return TopicBuilder.name(userRegisteredEvent).build();
    }

    @Bean
    public NewTopic checkBalanceEventTopic(){
        return TopicBuilder.name(checkBalanceEvent).build();
    }
}
