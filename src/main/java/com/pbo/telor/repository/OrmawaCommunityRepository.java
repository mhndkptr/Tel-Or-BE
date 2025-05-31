package com.pbo.telor.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pbo.telor.model.OrmawaCommunityEntity;

@Repository
public interface OrmawaCommunityRepository extends JpaRepository<OrmawaCommunityEntity, UUID> {
}
