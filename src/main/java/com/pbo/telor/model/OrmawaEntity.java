package com.pbo.telor.model;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.pbo.telor.enums.OrmawaCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ormawa")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class OrmawaEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description is required")
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Content is required")
    @Column(nullable = false)
    private String content;

    @NotNull(message = "IsOpenRegistration is required")
    @Column(nullable = false, unique = true)
    private boolean isOpenRegistration;

    @NotBlank(message = "Icon is required")
    @Column(nullable = false, unique = true)
    private String icon;

    @NotBlank(message = "Background is required")
    @Column(nullable = false, unique = true)
    private String background;

    @NotNull(message = "Category must be selected")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrmawaCategory category;

}
