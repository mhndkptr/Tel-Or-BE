package com.pbo.telor.repository;

import com.pbo.telor.model.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, UUID> {
  Page<FaqEntity> findAllByCategory(String category, Pageable pageable);

  Page<FaqEntity> findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCase(String question, String answer,
      Pageable pageable);
}