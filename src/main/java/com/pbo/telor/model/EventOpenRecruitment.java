package com.pbo.telor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "event_open_recruitments")
public class EventOpenRecruitment extends EventEntity {
    // Tambahkan atribut khusus open recruitment jika ada
}
