package com.miron.carting.domain;

import com.miron.carting.domain.base.ChangeableDateEntityListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_in_cart")
public class ProductInCart extends ChangeableDateEntityListener {
    @Id
    private UUID id;
    private int productId;
    private String name;
    private int count;
    private String description;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public ProductInCart(int productId) {
        this.productId = productId;
    }
}
