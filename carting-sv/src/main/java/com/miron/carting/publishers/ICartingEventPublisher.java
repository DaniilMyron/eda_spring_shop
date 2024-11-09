package com.miron.carting.publishers;

import com.miron.carting.domain.ProductInCart;
import com.miron.core.message.ChangeBalanceStatusEnum;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface ICartingEventPublisher {
    void publishBalanceEvent(int productId, Authentication authentication, int sum);

    void publishBuyingEvent(List<ProductInCart> productInCart, String username);

    void publishCancelBuyingEvent(Map<Integer, Integer> canceledProductsCount);

    void publishChangeBalanceEvent(String username, ChangeBalanceStatusEnum changeBalanceStatusEnum);
}
