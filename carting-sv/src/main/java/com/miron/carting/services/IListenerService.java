package com.miron.carting.services;

import com.miron.carting.controllers.model.PageResponse;
import com.miron.carting.controllers.model.ProductsInCartResponse;
import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.core.models.UserInfoForCheck;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface IListenerService {

    void buyFromCart(int productId, String username);

    void changeUserBalance(String username, ChangeBalanceStatusEnum changeBalanceStatusEnum);

    void changeUserBalance(String username, ChangeBalanceStatusEnum changeBalanceStatusEnum, JSONObject productsCountOnId);

    void cancellBuyingFromCart(JSONObject cancelledProductsCount);

    void makeCheck(UserInfoForCheck userInfo);

    void clearCartDeleteLeftoverProducts(UserInfoForCheck authenticatedUsername);

    void applyBuyingFromCart(JSONObject productsCountOnId);

    void returnProductsInCart(Map<Integer, Integer> productsCountOnId);

    void addProductToCart(JSONObject retrievedJsonObject);

    void createCartOnUser(JSONObject userJsonObject);
}
