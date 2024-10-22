package com.miron.security_lib.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deactivated_token")
public class DeactivatedToken {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "keep_until")
    private Date keepUntil;
}
