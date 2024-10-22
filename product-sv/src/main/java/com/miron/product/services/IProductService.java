package com.miron.product.services;

import com.miron.product.controllers.api.ProductRequest;
import com.miron.product.domain.Product;
import com.miron.product.services.models.IObjectFinder;
import com.miron.product.services.models.IProductPublisher;

public interface IProductService{
    void setFinder(IObjectFinder finder);
    void setPublisher(IProductPublisher publisher);
    void discardCartedProduct(Product product);
    Product findProductAndPublish(ProductRequest request, int count , Object auth);
}
