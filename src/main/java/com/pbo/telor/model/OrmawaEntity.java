package com.pbo.telor.model;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.pbo.telor.enums.OrmawaCategory;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ormawa")
public abstract class OrmawaEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Ormawa name is required")
    @Column(name = "ormawa_name", nullable = false)
    private String ormawaName;

    @NotBlank(message = "Description is required")
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotBlank(message = "Content is required")
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @NotNull(message = "Open registration flag is required")
    @Column(name = "is_open_registration", nullable = false)
    private Boolean isOpenRegistration;

    @NotBlank(message = "Icon is required")
    @Column(name = "icon", nullable = false)
    private String icon;

    @NotBlank(message = "Background is required")
    @Column(name = "background", nullable = false)
    private String background;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private OrmawaCategory category;
}
