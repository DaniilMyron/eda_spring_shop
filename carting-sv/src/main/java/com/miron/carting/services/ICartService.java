package com.miron.carting.services;

import com.miron.carting.controllers.model.PageResponse;
import com.miron.carting.controllers.model.ProductsInCartResponse;
import org.springframework.security.core.Authentication;

public interface ICartService {
    ProductsInCartResponse checkBalance(Authentication authentication, int productRequestId);

    PageResponse<ProductsInCartResponse> checkBalance(Authentication authentication, int page, int size);

    PageResponse<ProductsInCartResponse> findAllProductsInCart(Authentication authentication, int page, int size);
}
