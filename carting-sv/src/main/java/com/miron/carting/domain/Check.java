package com.miron.carting.domain;

import com.miron.carting.domain.base.ChangeableDateEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "checks")
public class Check extends ChangeableDateEntityListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int payingSum;
}
