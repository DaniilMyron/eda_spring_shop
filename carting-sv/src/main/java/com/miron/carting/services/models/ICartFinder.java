package com.miron.carting.services.models;

import com.miron.carting.domain.Cart;

public interface ICartFinder {
    Cart findCart(int cartId);
}
