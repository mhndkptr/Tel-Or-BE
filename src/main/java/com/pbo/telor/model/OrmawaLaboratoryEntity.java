package com.pbo.telor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "ormawa_laboratory")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class OrmawaLaboratoryEntity extends OrmawaEntity {

    @NotBlank(message = "Laboratory type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabType labType;

    public enum LabType {
        PRAKTIKUM,
        RESEARCH
    }
}
