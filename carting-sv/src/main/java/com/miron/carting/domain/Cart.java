package com.miron.carting.domain;


import com.miron.carting.domain.base.ChangeableDateEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart  extends ChangeableDateEntityListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int sum;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "cart")
    private List<ProductInCart> productInCart;
}
