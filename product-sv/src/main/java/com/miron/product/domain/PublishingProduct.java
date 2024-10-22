package com.miron.product.domain;

import com.miron.core.message.ProductOrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishingProduct {
    private int product_id;
    ProductOrderStatusEnum orderStatusEnum;
}
