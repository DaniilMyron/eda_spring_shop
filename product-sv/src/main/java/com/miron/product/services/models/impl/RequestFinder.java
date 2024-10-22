package com.miron.product.services.models.impl;

import com.miron.product.domain.Product;
import com.miron.product.repositories.ProductRepository;
import com.miron.product.services.models.IObjectFinder;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestFinder implements IObjectFinder {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product findRequestedObject(int productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        return product;
    }
}
