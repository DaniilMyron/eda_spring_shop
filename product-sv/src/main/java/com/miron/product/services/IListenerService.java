package com.miron.product.services;

import com.miron.product.controllers.api.ProductResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;

public interface IListenerService {
    void isCountValid(JSONArray productsInCartArray, String username);

    void returnCancelledProductsCount(JSONObject cancelledProductsInCart);
}
