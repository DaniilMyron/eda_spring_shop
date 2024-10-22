package com.miron.carting.services.impl;

import com.miron.carting.domain.Cart;
import com.miron.carting.repositories.CartRepository;
import com.miron.carting.repositories.ProductInCartRepository;
import com.miron.carting.repositories.UserRepository;
import com.miron.carting.services.ICartService;
import com.miron.carting.services.models.ICartPublisher;
import com.miron.carting.services.models.ICartFinder;
import com.miron.carting.services.models.impl.CartFinderImpl;
import com.miron.carting.services.models.impl.CartPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartService implements ICartService, InitializingBean {
    @Autowired
    private ProductInCartRepository productInCartRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    private ICartFinder finder;
    private ICartPublisher publisher;

    @Override
    public void setFinder(ICartFinder finder) {
        this.finder = finder;
    }

    @Override
    public void setPublisher(ICartPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public Cart findCart(int cartId) {
        return finder.findCart(cartId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setFinder(new CartFinderImpl(cartRepository));
        setPublisher(new CartPublisher());
    }
}
