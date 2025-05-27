package com.pbo.telor.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pbo.telor.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
}
