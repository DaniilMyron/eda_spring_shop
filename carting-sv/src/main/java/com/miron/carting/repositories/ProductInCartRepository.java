package com.miron.carting.repositories;

import com.miron.carting.domain.ProductInCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInCartRepository extends JpaRepository<ProductInCart, Integer> {
    ProductInCart findByProductId(int productId);
}
