package com.pbo.telor.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pbo.telor.enums.OrmawaCategory;
import com.pbo.telor.model.OrmawaEntity;

@Repository
public interface OrmawaRepository extends JpaRepository<OrmawaEntity, UUID> {
    List<OrmawaEntity> findByCategory(OrmawaCategory category);
    List<OrmawaEntity> findByIsOpenRegistrationTrue();
    List<OrmawaEntity> findByOrmawaNameContainingIgnoreCase(String ormawaName);
}
