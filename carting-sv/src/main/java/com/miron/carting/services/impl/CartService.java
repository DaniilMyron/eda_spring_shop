package com.miron.carting.services.impl;

import com.miron.carting.domain.Cart;
import com.miron.carting.domain.Product;
import com.miron.carting.domain.ProductInCart;
import com.miron.carting.repositories.CartRepository;
import com.miron.carting.repositories.ProductInCartRepository;
import com.miron.carting.repositories.ProductRepository;
import com.miron.carting.repositories.UserRepository;
import com.miron.carting.services.ICartService;
import com.miron.carting.publishers.ICartingEventPublisher;
import com.miron.carting.publishers.impl.CartingEventPublisher;
import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.core.models.UserInfoForCheck;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class CartService implements ICartService, InitializingBean {
    @Autowired
    private ProductInCartRepository productInCartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    @Setter
    private ICartingEventPublisher publisher;

    @Override
    public void checkBalance(Authentication authentication, int productRequestId) {
        Product product = productRepository.findById(productRequestId).orElseThrow();
        ProductInCart productInCart = productInCartRepository.findByProductId(productRequestId);
        this.publisher.publishBalanceEvent(productRequestId, authentication,product.getCost() * productInCart.getCount());
    }

    @Override
    public void checkBalance(Authentication authentication) {
        Cart cart = cartRepository.findByUserId(userRepository.findByUsername(authentication.getName()).getId());
        List<ProductInCart> productsInCart = productInCartRepository.findByCartId(cart.getId());
        this.publisher.publishBalanceEvent(0, authentication, productsInCart
                .stream()
                .mapToInt(p -> p.getCount() * productRepository.findById(p.getProductId()).orElseThrow().getCost())
                .sum());
    }

    @Override
    public void buyFromCart(int productId, String username) {
        if(productId == 0){
            Cart cart = cartRepository.findByUserId(userRepository.findByUsername(username).getId());
            List<ProductInCart> productsInCart = productInCartRepository.findByCartId(cart.getId());
            this.publisher.publishBuyingEvent(productsInCart, username);
        } else {
            ProductInCart productInCart = productInCartRepository.findByProductId(productId);
            this.publisher.publishBuyingEvent(List.of(productInCart), username);
        }
    }

    @Override
    public void changeUserBalance(String username, ChangeBalanceStatusEnum changeBalanceStatusEnum) {
        publisher.publishChangeBalanceEvent(username, changeBalanceStatusEnum);
    }

    @Override
    public void cancellBuyingFromCart(JSONObject canceledProductsCount) {
        var map = new HashMap<Integer, Integer>();
        Iterator<String> iterator = canceledProductsCount.keys();
        while(iterator.hasNext()){
            var key = iterator.next();
            map.put(Integer.parseInt(key), canceledProductsCount.getInt(key));
        }
        publisher.publishCancelBuyingEvent(map);
    }

    @Override
    public void makeCheck(UserInfoForCheck userInfo) {
        //TODO in DB save
    }

    @Override
    public void clearCart(UserInfoForCheck userInfo) {
        var cart = cartRepository.findByUserId(userRepository.findByUsername(userInfo.getAuthenticatedUsername()).getId());
        cartRepository.save(Cart.builder()
                .id(cart.getId())
                .sum(cart.getSum() - userInfo.getPayingSum())
                .user(cart.getUser())
                .build());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        setPublisher(new CartingEventPublisher());
    }
}
