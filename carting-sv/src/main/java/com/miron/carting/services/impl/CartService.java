package com.miron.carting.services.impl;

import com.miron.carting.domain.*;
import com.miron.carting.repositories.*;
import com.miron.carting.services.ICartService;
import com.miron.carting.publishers.ICartingEventPublisher;
import com.miron.carting.publishers.impl.CartingEventPublisher;
import com.miron.core.converter.ObjectToMapConverter;
import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.core.models.UserInfoForCheck;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private CheckRepository checkRepository;
    @Autowired
    private UsersChecksRepository usersChecksRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);
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
        publisher.publishChangeBalanceEvent(username, changeBalanceStatusEnum, null);
    }

    @Override
    public void changeUserBalance(String username, ChangeBalanceStatusEnum changeBalanceStatusEnum, JSONObject productsCountOnId) {
        var map = ObjectToMapConverter.convertJSONObjectToMap(productsCountOnId);
        publisher.publishChangeBalanceEvent(username, changeBalanceStatusEnum, map);
    }

    @Override
    public void cancellBuyingFromCart(JSONObject productsCountOnId) {
        var map = ObjectToMapConverter.convertJSONObjectToMap(productsCountOnId);
        publisher.publishCancelBuyingEvent(map);
    }

    @Override
    public void applyBuyingFromCart(JSONObject productsCountOnId) {
        var productIdCountMap = ObjectToMapConverter.convertJSONObjectToMap(productsCountOnId);
        for (Integer key : productIdCountMap.keySet()) {
            var productInCart = productInCartRepository.findByProductId(key);
            productInCart.setCount(productInCart.getCount() - productIdCountMap.get(key));
            productInCartRepository.save(productInCart);
        }
    }

    @Override
    public void returnProductsInCart(Map<Integer, Integer> productsCountOnId) {
        for (Integer key : productsCountOnId.keySet()) {
            var productInCart = productInCartRepository.findByProductId(key);
            productInCart.setCount(productInCart.getCount() + productsCountOnId.get(key));
            productInCartRepository.save(productInCart);
        }
    }

    @Override
    public void makeCheck(UserInfoForCheck userInfo) {
        var check = checkRepository.save(Check.builder()
                .payingSum(userInfo.getPayingSum())
                .build()
        );
        var user = userRepository.findByUsername(userInfo.getAuthenticatedUsername());
        usersChecksRepository.save(UsersChecks.builder()
                .check(check)
                .user(user)
                .build()
        );
        LOGGER.info("Made check with user id: {}, check id: {}, paying sum: {}", user.getId(), check.getId(), check.getPayingSum());
    }

    @Override
    public void clearCartDeleteLeftoverProducts(UserInfoForCheck userInfo) {
        var cart = cartRepository.findByUserId(userRepository.findByUsername(userInfo.getAuthenticatedUsername()).getId());
        cartRepository.save(Cart.builder()
                .id(cart.getId())
                .sum(cart.getSum() - userInfo.getPayingSum())
                .user(cart.getUser())
                .build());

        for(ProductInCart productInCart : productInCartRepository.findByCartId(cart.getId())){
            if(productInCart.getCount() == 0){
                productInCartRepository.delete(productInCart);
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        setPublisher(new CartingEventPublisher());
    }
}
