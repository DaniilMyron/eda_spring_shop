package com.miron.carting.converters;

import com.miron.carting.controllers.model.ProductsInCartResponse;
import com.miron.carting.domain.ProductInCart;

import java.util.function.Function;

public class ProductsInCartToResponseConverter implements Function<ProductInCart, ProductsInCartResponse> {

    @Override
    public ProductsInCartResponse apply(ProductInCart productInCart) {
        return ProductsInCartResponse.builder()
                .id(productInCart.getId())
                .productId(productInCart.getProductId())
                .name(productInCart.getName())
                .description(productInCart.getDescription())
                .count(productInCart.getCount())
                .build();
    }
}
