package com.miron.carting.repositories;

import com.miron.carting.domain.ProductInCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInCartRepository extends JpaRepository<ProductInCart, Integer> {
    ProductInCart findByProductId(int productId);
    List<ProductInCart> findByCartId(int cartId);
}
