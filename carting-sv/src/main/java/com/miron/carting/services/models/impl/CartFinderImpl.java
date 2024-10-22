package com.miron.carting.services.models.impl;

import com.miron.carting.domain.Cart;
import com.miron.carting.repositories.CartRepository;
import com.miron.carting.services.models.ICartFinder;

public class CartFinderImpl implements ICartFinder {
    private final CartRepository cartRepository;

    public CartFinderImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart findCart(int cartId) {
        return cartRepository.findById(cartId).orElseThrow();
    }
}
