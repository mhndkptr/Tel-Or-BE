package com.pbo.telor.repository;

import com.pbo.telor.model.TokenBlacklistEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklistEntity, UUID> {
    Optional<TokenBlacklistEntity> findByToken(String token);
    boolean existsByToken(String token);
}