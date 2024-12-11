package com.miron.carting.domain;

import com.miron.carting.domain.base.ChangeableDateEntityListener;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "users")
public class User extends ChangeableDateEntityListener {
    @Id
    private int id;
    private String username;
}
