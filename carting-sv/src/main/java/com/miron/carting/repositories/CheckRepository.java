package com.miron.carting.repositories;

import com.miron.carting.domain.Check;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckRepository extends JpaRepository<Check, Integer> {
}
