package com.miron.product.services;

import com.miron.product.controllers.api.ProductRequest;
import com.miron.product.domain.Product;
import org.json.JSONArray;
import org.json.JSONObject;

public interface IProductService{
    void discardCartedProduct(Product product);
    Product findProductAndPublish(ProductRequest request, int count , Object auth);

    void isCountValid(JSONArray productsInCartArray, String username);

    void returnCancelledProductsCount(JSONObject cancelledProductsInCart);
}
