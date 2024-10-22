package com.miron.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishedProduct {
    private int id;
    private String name;
    private int cost;
    private int count;
    private String description;
    private String authenticatedUsername;
}
