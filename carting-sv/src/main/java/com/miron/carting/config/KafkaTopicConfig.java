package com.miron.carting.config;

import com.miron.carting.publishers.ICartingEventPublisher;
import com.miron.carting.publishers.impl.CartingEventPublisher;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaTopicConfig {
    @Value("${carting-sv.topic.produces.checkBalanceEvent}")
    private String checkBalanceEvent;

    @Bean
    public ICartingEventPublisher cartingEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        return new CartingEventPublisher(kafkaTemplate);
    }

    @Bean
    public NewTopic checkBalanceEventTopic() {
        return TopicBuilder.name(checkBalanceEvent).build();
    }
}
