package com.pbo.telor.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pbo.telor.enums.EventType;
import com.pbo.telor.model.EventEntity;


@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID>, JpaSpecificationExecutor<EventEntity> {
    List<EventEntity> findByEventNameContainingIgnoreCase(String eventName);
    List<EventEntity> findByEventType(EventType eventType);
    @Query("SELECT e FROM EventEntity e WHERE e.startEvent >= :startDate AND e.endEvent <= :endDate")
    List<EventEntity> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    List<EventEntity> findTop3ByOrderByStartEventDesc();
}
