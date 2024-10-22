package com.miron.carting.consumersTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.carting.domain.Cart;
import com.miron.carting.listeners.ProductPublishedListener;
import com.miron.carting.repositories.CartRepository;
import com.miron.carting.repositories.UserRepository;
import com.miron.core.message.ProductOrderCreatedEvent;
import com.miron.core.message.ProductOrderStatusEnum;
import com.miron.core.models.PublishedProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://172.28.141.236:9092", "port=9092" })
public class CartingProductListener {
    private User user;
    @Autowired
    private ProductPublishedListener listener;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

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
    public void cartingProduct() throws JsonProcessingException {
        com.miron.carting.domain.User user = new com.miron.carting.domain.User(1, "danya1");
        userRepository.save(user);
        var cart = cartRepository.findByUserId(user.getId());
        if(cart == null) {
            cartRepository.save(Cart.builder()
                    .user(user)
                    .sum(0)
                    .build());
        }

        ProductFromService product = new ProductFromService(1, "someName", 100, 0, "some Description");

        PublishedProduct publishedProduct = PublishedProduct.builder()
                .id(product.getId())
                .cost(product.getCost())
                .count(product.getCount())
                .description(product.getDescription())
                .name(product.getName())
                .authenticatedUsername(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                .build();
        final var productOrderCreatedEvent =
                new ProductOrderCreatedEvent(publishedProduct, 50, LocalDateTime.now(), ProductOrderStatusEnum.CREATED);
        final String payload = objectMapper.writeValueAsString(productOrderCreatedEvent);

        listener.listens(payload);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductFromService {
        private int id;
        private String name;
        private int cost;
        private int count;
        private String description;
    }
}
