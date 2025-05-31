package com.pbo.telor.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pbo.telor.model.OrmawaUKMEntity;

@Repository
public interface OrmawaUKMRepository extends JpaRepository<OrmawaUKMEntity, UUID> {
    List<OrmawaUKMEntity> findByUkmCategory(String ukmCategory);
}
