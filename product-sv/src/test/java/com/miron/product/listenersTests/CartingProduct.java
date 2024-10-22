package com.miron.product.listenersTests;

import com.miron.product.controllers.api.ProductRequest;
import com.miron.product.services.impl.ProductService;
import com.miron.product.services.models.impl.JSONPublisher;
import com.miron.product.services.models.impl.RequestFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://172.28.141.236:9092", "port=9092" })
class CartingProduct {
    @Autowired
    private ProductService productService;
    private User user;
    @Autowired
    private RequestFinder requestFinder;
    @Autowired
    private JSONPublisher jsonPublisher;

    @BeforeEach
    public void setup() {
        user = (User) User.builder()
                .username("danya1")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    @WithMockUser(username = "danya1", roles = "USER")
    public void cartingProduct() {
        productService.setFinder(requestFinder);
        productService.setPublisher(jsonPublisher);
        productService.findProductAndPublish(new ProductRequest(1), 100, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
