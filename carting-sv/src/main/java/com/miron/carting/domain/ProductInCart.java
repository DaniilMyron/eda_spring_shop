package com.miron.carting.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_in_cart")
public class ProductInCart {
    @Id
    private UUID id;
    private int productId;
    private String name;
    private int count;
    private String description;
    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public ProductInCart(int productId) {
        this.productId = productId;
    }
}
