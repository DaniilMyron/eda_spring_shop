package com.miron.core.models;

import lombok.*;

import java.util.Map;

@Data
@Builder
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
public class ProductsInCartToReturn {
    private Map<Integer, Integer> productsCountOnId;
}
