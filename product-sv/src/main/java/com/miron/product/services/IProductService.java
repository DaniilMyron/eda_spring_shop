package com.miron.product.services;

import com.miron.product.controllers.api.ProductResponse;
import org.springframework.security.core.Authentication;

public interface IProductService {
    ProductResponse findProductAndCart(int productId, int count , Authentication auth);

}
