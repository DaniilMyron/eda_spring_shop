package com.miron.carting.services;

import com.miron.carting.domain.Cart;
import com.miron.carting.services.models.ICartFinder;
import com.miron.carting.services.models.ICartPublisher;

public interface ICartService {
    void setFinder(ICartFinder finder);
    void setPublisher(ICartPublisher publisher);
    Cart findCart(int cartId);
}
