package com.miron.carting.repositories;

import com.miron.carting.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUserId(int userId);
}
