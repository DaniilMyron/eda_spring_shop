package com.miron.product.services.impl;

import com.miron.product.controllers.api.ProductResponse;
import com.miron.product.converter.ProductToResponseConverter;
import com.miron.product.exceptions.ProductNotFoundException;
import com.miron.product.publishers.IProductEventPublisher;
import com.miron.product.repositories.ProductRepository;
import com.miron.product.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
    private final IProductEventPublisher publisher;
    private final ProductRepository productRepository;
    private final ProductToResponseConverter productToResponseConverter = new ProductToResponseConverter();

    @Override
    public ProductResponse findProductAndCart(int productId, int count, Authentication auth) {
        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        publisher.publishOrderCreatingEvent(product, count, auth);
        return productToResponseConverter.apply(product);
    }
}
