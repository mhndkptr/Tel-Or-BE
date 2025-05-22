package com.pbo.telor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ormawa_ukm")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class OrmawaUKMEntity extends OrmawaEntity {
    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String ukmCategory;
}