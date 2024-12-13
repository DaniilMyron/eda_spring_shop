package com.miron.product.converter;

import com.miron.product.controllers.api.ProductResponse;
import com.miron.product.domain.Product;

import java.util.function.Function;

public class ProductToResponseConverter implements Function<Product, ProductResponse> {

    @Override
    public ProductResponse apply(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .cost(product.getCost())
                .description(product.getDescription())
                .build();
    }
}
