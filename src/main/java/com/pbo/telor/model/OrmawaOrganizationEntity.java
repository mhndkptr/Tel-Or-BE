package com.pbo.telor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "ormawa_community")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)

public class OrmawaOrganizationEntity extends OrmawaEntity {
}
