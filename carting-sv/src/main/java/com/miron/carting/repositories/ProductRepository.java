package com.miron.carting.repositories;

import com.miron.carting.domain.Product;
import com.miron.carting.domain.ProductInCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
