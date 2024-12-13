package com.miron.carting.services.impl;

import com.miron.carting.controllers.model.PageResponse;
import com.miron.carting.controllers.model.ProductsInCartResponse;
import com.miron.carting.converters.ProductsInCartToResponseConverter;
import com.miron.carting.domain.Cart;
import com.miron.carting.domain.Product;
import com.miron.carting.domain.ProductInCart;
import com.miron.carting.domain.User;
import com.miron.carting.exceptions.CartNotFoundException;
import com.miron.carting.exceptions.ProductInCartNotFoundException;
import com.miron.carting.exceptions.ProductNotFoundException;
import com.miron.carting.exceptions.UserNotFoundException;
import com.miron.carting.publishers.ICartingEventPublisher;
import com.miron.carting.repositories.*;
import com.miron.carting.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService {
    private final ProductInCartRepository productInCartRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ICartingEventPublisher publisher;
    private final ProductsInCartToResponseConverter productsInCartToResponseConverter = new ProductsInCartToResponseConverter();
    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    @Override
    public PageResponse<ProductsInCartResponse> findAllProductsInCart(Authentication authentication, int page, int size) {
        Page<ProductInCart> pageOfProducts = pageableProductsToList(authentication, page, size);
        List<ProductsInCartResponse> listOfProducts = productsListFromPage(pageOfProducts);

        return new PageResponse<>(
                listOfProducts,
                pageOfProducts.getNumber(),
                pageOfProducts.getSize(),
                pageOfProducts.getTotalElements(),
                pageOfProducts.getTotalPages(),
                pageOfProducts.isFirst(),
                pageOfProducts.isLast()
        );
    }

    @Override
    public ProductsInCartResponse checkBalance(Authentication authentication, int productRequestId) {
        Product product = productRepository.findById(productRequestId).orElseThrow(ProductNotFoundException::new);
        ProductInCart productInCart = productInCartRepository.findByProductId(productRequestId)
                .orElseThrow(ProductInCartNotFoundException::new);
        this.publisher.publishBalanceEvent(productRequestId, authentication,product.getCost() * productInCart.getCount());
        return productsInCartToResponseConverter.apply(productInCart);
    }

    @Override
    public PageResponse<ProductsInCartResponse> checkBalance(Authentication authentication, int page, int size) {
        Page<ProductInCart> pageOfProducts = pageableProductsToList(authentication, page, size);
        List<ProductsInCartResponse> listOfProducts = productsListFromPage(pageOfProducts);

        Cart cart = cartRepository
                .findByUserId(userRepository
                        .findByUsername(authentication
                                .getName())
                        .orElseThrow(UserNotFoundException::new)
                        .getId())
                .orElseThrow(CartNotFoundException::new);
        List<ProductInCart> productsInCart = productInCartRepository.findByCartId(cart.getId());
        this.publisher.publishBalanceEvent(0, authentication, productsInCart
                .stream()
                .mapToInt(product -> product.getCount() *
                        productRepository
                                .findById(product
                                        .getProductId())
                                .orElseThrow(ProductNotFoundException::new)
                                .getCost())
                .sum());

        return new PageResponse<>(
                listOfProducts,
                pageOfProducts.getNumber(),
                pageOfProducts.getSize(),
                pageOfProducts.getTotalElements(),
                pageOfProducts.getTotalPages(),
                pageOfProducts.isFirst(),
                pageOfProducts.isLast()
        );
    }

    private Page<ProductInCart> pageableProductsToList(Authentication authentication, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(CartNotFoundException::new);
        return productInCartRepository.findAllProductsByCart(pageable, cart);
    }

    private List<ProductsInCartResponse> productsListFromPage(Page<ProductInCart> allProductsByCart) {
        List<ProductsInCartResponse> productsInCartResponse = allProductsByCart.stream()
                .map(productsInCartToResponseConverter)
                .toList();

        productsInCartResponse.forEach(product -> LOGGER.info("Product in cart info: {}", product));
        return productsInCartResponse;
    }
}
