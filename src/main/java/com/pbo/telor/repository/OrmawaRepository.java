package com.pbo.telor.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pbo.telor.enums.LabType;
import com.pbo.telor.enums.OrmawaCategory;
import com.pbo.telor.model.OrmawaEntity;
import com.pbo.telor.model.OrmawaLaboratoryEntity;
import com.pbo.telor.model.OrmawaUKMEntity;

@Repository
public interface OrmawaRepository extends JpaRepository<OrmawaEntity, UUID>, JpaSpecificationExecutor<OrmawaEntity> {
    List<OrmawaEntity> findByOrmawaNameContainingIgnoreCase(String ormawaName);
    List<OrmawaEntity> findByCategory(OrmawaCategory category);
    List<OrmawaEntity> findByIsOpenRegistrationTrue();
    @Query(value = "SELECT o.* FROM ormawa o LIMIT 3", nativeQuery = true)
    List<OrmawaEntity> findTop3Ormawa();
    List<OrmawaLaboratoryEntity> findByLabType(LabType labType);
    List<OrmawaUKMEntity> findByUkmCategory(String ukmCategory);
}
