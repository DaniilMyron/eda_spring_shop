package com.miron.carting.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsInCartResponse {
    private UUID id;
    private int productId;
    private String name;
    private int count;
    private String description;
}
