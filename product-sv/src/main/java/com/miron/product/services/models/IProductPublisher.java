package com.miron.product.services.models;

import com.miron.product.domain.Product;

public interface IProductPublisher {
    void publish(Product product, int count, Object auth);
}
