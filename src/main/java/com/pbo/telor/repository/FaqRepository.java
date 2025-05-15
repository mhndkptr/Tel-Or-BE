package com.pbo.telor.repository;

import com.pbo.telor.model.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, UUID> {
} 