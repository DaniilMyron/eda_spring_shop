package com.miron.carting.services;

import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.core.models.UserInfoForCheck;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface ICartService {
    void checkBalance(Authentication authentication, int productRequestId);
    void checkBalance(Authentication authentication);

    void buyFromCart(int productId, String username);

    void changeUserBalance(String username, ChangeBalanceStatusEnum changeBalanceStatusEnum);
    void changeUserBalance(String username, ChangeBalanceStatusEnum changeBalanceStatusEnum, JSONObject productsCountOnId);

    void cancellBuyingFromCart(JSONObject cancelledProductsCount);

    void makeCheck(UserInfoForCheck userInfo);

    void clearCartDeleteLeftoverProducts(UserInfoForCheck authenticatedUsername);

    void applyBuyingFromCart(JSONObject productsCountOnId);

    void returnProductsInCart(Map<Integer, Integer> productsCountOnId);
}
