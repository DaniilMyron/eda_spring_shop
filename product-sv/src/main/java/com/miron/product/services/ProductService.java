package com.miron.product.services;

import com.miron.product.controllers.api.ProductRequest;
import com.miron.product.domain.Product;
import com.miron.product.exceptions.ProductNotFoundException;
import com.miron.product.repositories.ProductRepository;
import com.miron.product.publishers.IProductEventPublisher;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final IProductEventPublisher publisher;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void discardCartedProduct(Product product) {
        product.setCount(product.getCount() - 1);
        productRepository.saveAndFlush(product);
    }

    @Override
    public Product findProductAndPublish(ProductRequest request, int count, Object auth) {
        var product = productRepository.findById(request.id()).orElseThrow(ProductNotFoundException::new);
        publisher.publishOrderCreatingEvent(product, count, auth);
        return product;
    }

    @Override
    public void isCountValid(JSONArray productsInCartArray, String username) {
        List<Product> validCountProducts = new ArrayList<>();
        List<Product> invalidCountProducts = new ArrayList<>();
        Map<Integer, Integer> productsCountArray = new HashMap<>();
        for(int i = 0; i < productsInCartArray.length(); i++) {
            var productId = productsInCartArray.getJSONObject(i).getInt("productId");
            var productsCount = productsInCartArray.getJSONObject(i).getInt("count");
            productsCountArray.put(productId, productsCount);
            var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
            if(product.getCount() - productsCount < 0) {
                invalidCountProducts.add(product);
            }
            validCountProducts.add(product);
        }

        if (invalidCountProducts.isEmpty()) {
            publisher.publishBuyingFromCartEventResult(validCountProducts, true, username, productsCountArray);
            minusProductCount(validCountProducts, productsCountArray);
        } else {
            publisher.publishBuyingFromCartEventResult(invalidCountProducts, false, username, productsCountArray);
        }
    }

    @Override
    public void returnCancelledProductsCount(JSONObject cancelledProductsInCart) {
        Iterator<String> iterator = cancelledProductsInCart.keys();
        while(iterator.hasNext()){
            var key = iterator.next();
            var product = productRepository.findById(Integer.parseInt(key)).orElseThrow(ProductNotFoundException::new);
            product.setCount(product.getCount() + cancelledProductsInCart.getInt(key));
            productRepository.saveAndFlush(product);
        }
    }

    private void minusProductCount(List<Product> validCountProducts, Map<Integer, Integer> countArray) {
        int iteration = 0;
        for(Product product : validCountProducts) {
            product.setCount(product.getCount() - countArray.get(product.getId()));
            productRepository.saveAndFlush(product);
            validCountProducts.set(iteration, product);
            iteration++;
        }
    }
}
