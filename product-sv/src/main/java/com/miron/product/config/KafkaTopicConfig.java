package com.miron.product.config;

import com.miron.product.publishers.IProductEventPublisher;
import com.miron.product.publishers.impl.ProductEventPublisher;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaTopicConfig {
    @Value("${product-sv.topic.produces.addProductToCartEvent}")
    private String addProductToCartEvent;

    @Bean
    public IProductEventPublisher productEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        return new ProductEventPublisher(kafkaTemplate);
    }

    @Bean
    public NewTopic mironTopic(){
        return TopicBuilder.name(addProductToCartEvent).build();
    }
}
