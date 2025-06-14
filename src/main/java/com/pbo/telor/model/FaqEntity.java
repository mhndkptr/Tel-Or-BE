package com.pbo.telor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faqs")
public class FaqEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Question is required")
    @Column(name = "question", nullable = false)
    private String question;

    @NotBlank(message = "Answer is required")
    @Column(name = "answer", nullable = false)
    private String answer;

    @NotBlank(message = "Category is required")
    @Column(name = "category", nullable = false)
    private String category;
}