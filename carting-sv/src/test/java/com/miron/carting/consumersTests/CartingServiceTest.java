package com.miron.carting.consumersTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.miron.carting.services.impl.CartService;
import com.miron.core.models.UserInfoForCheck;
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
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://172.28.141.236:9092", "port=9092" })
@ActiveProfiles("test")
public class CartingServiceTest {
    private User user;
    @Autowired
    private CartService cartService;

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
    public void listenerAuthContext() throws JsonProcessingException {
        cartService.makeCheck(new UserInfoForCheck(150, SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
