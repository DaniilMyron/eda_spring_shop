package com.miron.product.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miron.core.message.ProductOrderStatusEnum;
import com.miron.product.domain.PublishingProduct;
import com.miron.product.exceptions.InvalidMessageException;
import com.miron.product.exceptions.ProductPublishException;
import com.miron.product.repositories.ProductRepository;
import com.miron.product.services.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CartingProductEventResult {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "miron-product-carting-result", groupId = "groupId")
    public void listens(final String serializedProduct) {
        log.info("Received Product: {}", serializedProduct);
        try {
            JSONObject jsonObject = new JSONObject(serializedProduct);
            final var deserializedProduct = deserializeProductFromPayload(jsonObject);
            var product = productRepository.findById(deserializedProduct.getProduct_id()).orElseThrow();
            log.info("Product {} persisted!", product.getId());

            if(deserializedProduct.getOrderStatusEnum() == ProductOrderStatusEnum.CONFIRMED){
                productService.discardCartedProduct(product);
            } else{
                throw new ProductPublishException("Invalid status for publishing order to cart" ,deserializedProduct);
            }
        } catch(final InvalidMessageException ex) {
            log.error("Invalid message received: {}", serializedProduct);
        }
    }

    private PublishingProduct deserializeProductFromPayload(final JSONObject payload) {
        return PublishingProduct.builder()
                .product_id(payload.getInt("product_id"))
                .orderStatusEnum(payload.getEnum(ProductOrderStatusEnum.class, "orderStatusEnum"))
                .build();
    }
}
