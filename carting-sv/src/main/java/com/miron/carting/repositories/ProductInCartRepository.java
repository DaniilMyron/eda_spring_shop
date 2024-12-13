package com.miron.carting.repositories;

import com.miron.carting.domain.Cart;
import com.miron.carting.domain.ProductInCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductInCartRepository extends JpaRepository<ProductInCart, Integer>, JpaSpecificationExecutor<ProductInCart> {
    Optional<ProductInCart> findByProductId(int productId);
    List<ProductInCart> findByCartId(int cartId);

    @Query("""
            SELECT productInCart
            FROM ProductInCart productInCart
            WHERE productInCart.cart = :cart
            """)
    Page<ProductInCart> findAllProductsByCart(Pageable pageable, @Param("cart") Cart cart);

    Optional<ProductInCart> findFirstByCart(Cart cart);
}
