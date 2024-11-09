package com.miron.core.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miron.core.models.PublishedProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderCreatedEvent implements EventMessage{
    private PublishedProduct publishedProduct;
    private int count;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime timestamp;
    private ProductOrderStatusEnum status;
}
