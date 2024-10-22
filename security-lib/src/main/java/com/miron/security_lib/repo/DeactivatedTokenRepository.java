package com.miron.security_lib.repo;

import com.miron.security_lib.models.DeactivatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeactivatedTokenRepository extends JpaRepository<DeactivatedToken, Integer> {
}
