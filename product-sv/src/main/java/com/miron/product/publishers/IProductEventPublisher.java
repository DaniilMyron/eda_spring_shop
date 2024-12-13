package com.miron.product.publishers;

import com.miron.product.domain.Product;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface IProductEventPublisher {
    void publishOrderCreatingEvent(Product product, int count, Authentication auth);

    void publishBuyingFromCartEventResult(List<Product> invalidCountProducts, boolean isConfirmed, String username, Map<Integer, Integer> count);
}
