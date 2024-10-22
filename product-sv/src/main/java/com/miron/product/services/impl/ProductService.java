package com.miron.product.services.impl;

import com.miron.product.controllers.api.ProductRequest;
import com.miron.product.domain.Product;
import com.miron.product.repositories.ProductRepository;
import com.miron.product.services.IProductService;
import com.miron.product.services.models.IObjectFinder;
import com.miron.product.services.models.IProductPublisher;
import com.miron.product.services.models.impl.JSONPublisher;
import com.miron.product.services.models.impl.RequestFinder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService, InitializingBean {
    @Autowired
    private IProductPublisher publisher;
    @Autowired
    private IObjectFinder finder;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void setFinder(IObjectFinder finder) {
        this.finder = finder;
    }

    @Override
    public void setPublisher(IProductPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void discardCartedProduct(Product product) {
        product.setCount(product.getCount() - 1);
        productRepository.saveAndFlush(product);
    }

    @Override
    public Product findProductAndPublish(ProductRequest request, int count, Object auth) {
        var product = (Product) finder.findRequestedObject(request.id());
        publisher.publish(product, count, auth);
        return product;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setFinder(new RequestFinder());
        setPublisher(new JSONPublisher());
    }
}
