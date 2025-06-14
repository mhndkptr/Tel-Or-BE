package com.pbo.telor.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pbo.telor.model.OrmawaLaboratoryEntity;

@Repository
public interface OrmawaLaboratoryRepository extends JpaRepository<OrmawaLaboratoryEntity, UUID> {
    List<OrmawaLaboratoryEntity> findByLabType(com.pbo.telor.enums.LabType labType);
}
