package com.miron.product.listenersTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miron.core.converter.StringPayloadDeserializer;
import com.miron.core.message.BuyingFromCartEvent;
import com.miron.core.message.BuyingFromCartEventResult;
import com.miron.core.message.BuyingFromCartStatusEnum;
import com.miron.core.message.CancelBuyingFromCartEvent;
import com.miron.product.listeners.BuyingFromCartEventCreatedListener;
import com.miron.product.listeners.CancelBuyingFromCartEventListener;
import com.miron.product.services.impl.ProductService;
import com.miron.product.publishers.impl.ProductEventPublisher;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://172.28.141.236:9092", "port=9092" })
class CartingProductTest {
    @Autowired
    private ProductService productService;

    private User user;

    @Autowired
    private ProductEventPublisher productEventPublisher;
    @Autowired
    private BuyingFromCartEventCreatedListener buyingFromCartEventListener;
    @Autowired
    private CancelBuyingFromCartEventListener cancelBuyingFromCartEventListener;
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
        //TODO random objects
        var buyingEvent = new BuyingFromCartEvent(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                List.of(new ProductFromService(1, "lol", 2, "dsa")),
                LocalDateTime.now(),
                BuyingFromCartStatusEnum.CREATED);
        final String payload = objectMapper.writeValueAsString(buyingEvent);
        buyingFromCartEventListener.listens(payload);


//        productService.setFinder(requestFinder);
//        productService.setPublisher(jsonPublisher);
//        productService.findProductAndPublish(new ProductRequest(1), 100, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    @WithMockUser(username = "danya1", roles = "USER")
    public void cancelBuyingFromCartEventListener() throws JsonProcessingException {
        var map = new HashMap<Integer, Integer>();
        map.put(1, 100);
        map.put(2, 35);
        map.put(3, 1);
        var buyingEvent = new BuyingFromCartEventResult(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                List.of(new ProductFromService(1, "lol", 2, "dsa")),
                LocalDateTime.now(),
                BuyingFromCartStatusEnum.CREATED,
                map);
        final String payload = objectMapper.writeValueAsString(buyingEvent);
        var readStringAsJSONObject = StringPayloadDeserializer.readStringAsJSONObject(payload);
        var json = readStringAsJSONObject.getJSONObject("count");
        var newMap = new HashMap<Integer, Integer>();
        Iterator<String> iterator = json.keys();
        while(iterator.hasNext()){
            //System.out.println(iterator.next());
            var key = iterator.next();
            newMap.put(Integer.parseInt(key), json.getInt(key));
        }
        var cancellBuyingFromCartEvent = new CancelBuyingFromCartEvent(
                newMap,
                LocalDateTime.now());
        final String cancelledPayload = objectMapper.writeValueAsString(cancellBuyingFromCartEvent);
        cancelBuyingFromCartEventListener.listens(cancelledPayload);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductFromService {
        private int productId;
        private String name;
        private int count;
        private String description;
    }
}
