package com.pbo.telor.model;

import com.pbo.telor.enums.LabType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ormawa_laboratories")
public class OrmawaLaboratoryEntity extends OrmawaEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "lab_type", nullable = false)
    private LabType labType;
}
