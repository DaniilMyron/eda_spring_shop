package com.miron.carting.domain;

import com.miron.carting.domain.base.ChangeableDateEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends ChangeableDateEntityListener {
    @Id
    private int id;
    private String name;
    private int cost;
    private String description;
}
