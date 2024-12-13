package com.miron.carting.services.impl;

import com.miron.carting.converters.ObjectToProductConverter;
import com.miron.carting.converters.ObjectToUserConverter;
import com.miron.carting.domain.*;
import com.miron.carting.exceptions.CartNotFoundException;
import com.miron.carting.exceptions.ProductInCartNotFoundException;
import com.miron.carting.exceptions.UserNotFoundException;
import com.miron.carting.repositories.*;
import com.miron.carting.publishers.ICartingEventPublisher;
import com.miron.carting.services.IListenerService;
import com.miron.core.converter.ObjectToMapConverter;
import com.miron.core.converter.UsernameDeserializer;
import com.miron.core.message.ChangeBalanceStatusEnum;
import com.miron.core.models.UserInfoForCheck;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ListenerService implements IListenerService {
    private final ProductInCartRepository productInCartRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CheckRepository checkRepository;
    private final UsersChecksRepository usersChecksRepository;
    private final ICartingEventPublisher publisher;
    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerService.class);


    @Override
    public void addProductToCart(JSONObject retrievedJsonObject) {
        var payload = retrievedJsonObject.getJSONObject("publishedProduct");
        var payloadCount = retrievedJsonObject.getInt("count");

        var username = payload.getString("authenticatedUsername");
        username = UsernameDeserializer.readUsernameFromPayload(username);

        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        var cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);

        var productToCart = ObjectToProductConverter.productToCartFromPayload(payload, payloadCount, cart);
        var product = ObjectToProductConverter.productFromPayload(payload);

        var foundedProduct = productInCartRepository.findByProductId(productToCart.getProductId())
                .orElseThrow(ProductInCartNotFoundException::new);
        if(foundedProduct != null){
            foundedProduct.setCount(payloadCount + foundedProduct.getCount());
            productInCartRepository.save(foundedProduct);
        } else {
            productInCartRepository.save(productToCart);
        }
        productRepository.save(product);

        cartRepository.save(Cart.builder()
                .id(cart.getId())
                .sum(cart.getSum() + payloadCount * payload.getInt("cost"))
                .user(cart.getUser())
                .build());

        LOGGER.info("Product saved to cart: {}", productToCart);
    }

    @Override
    public void createCartOnUser(JSONObject userJsonObject) {
        var user = ObjectToUserConverter.userFromPayload(userJsonObject.getJSONObject("publishedUser"));
        userRepository.save(user);
        var cart = cartRepository.save(Cart.builder()
                .user(user)
                .sum(0)
                .build());
        LOGGER.info("User saved to DB: {}, cart saved to DB: {}", user, cart);
    }

    @Override
    public void buyFromCart(int productId, String username) {
        if(productId == 0){
            Cart cart = cartRepository
                    .findByUserId(userRepository
                            .findByUsername(username)
                            .orElseThrow(UserNotFoundException::new)
                            .getId())
                    .orElseThrow(CartNotFoundException::new);
            List<ProductInCart> productsInCart = productInCartRepository.findByCartId(cart.getId());
            this.publisher.publishBuyingEvent(productsInCart, username);
        } else {
            ProductInCart productInCart = productInCartRepository.findByProductId(productId)
                    .orElseThrow(ProductInCartNotFoundException::new);
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
            var productInCart = productInCartRepository.findByProductId(key)
                    .orElseThrow(ProductInCartNotFoundException::new);
            productInCart.setCount(productInCart.getCount() - productIdCountMap.get(key));
            productInCartRepository.save(productInCart);
        }
    }

    @Override
    public void returnProductsInCart(Map<Integer, Integer> productsCountOnId) {
        for (Integer key : productsCountOnId.keySet()) {
            var productInCart = productInCartRepository.findByProductId(key)
                    .orElseThrow(ProductInCartNotFoundException::new);
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
        var user = userRepository.findByUsername(userInfo.getAuthenticatedUsername())
                .orElseThrow(UserNotFoundException::new);
        usersChecksRepository.save(UsersChecks.builder()
                .check(check)
                .user(user)
                .build()
        );
        LOGGER.info("Made check with user id: {}, check id: {}, paying sum: {}", user.getId(), check.getId(), check.getPayingSum());
    }

    @Override
    public void clearCartDeleteLeftoverProducts(UserInfoForCheck userInfo) {
        var cart = cartRepository
                .findByUserId(userRepository
                        .findByUsername(userInfo
                                .getAuthenticatedUsername())
                        .orElseThrow(UserNotFoundException::new)
                        .getId())
                .orElseThrow(CartNotFoundException::new);
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
}
