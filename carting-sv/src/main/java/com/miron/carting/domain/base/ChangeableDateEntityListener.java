package com.miron.carting.domain.base;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class ChangeableDateEntityListener {

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;
}
