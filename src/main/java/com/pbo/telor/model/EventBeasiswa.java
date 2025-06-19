package com.pbo.telor.model;

import com.pbo.telor.enums.EventRegion;

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
@Table(name = "event_beasiswas")
public class EventBeasiswa extends EventEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "event_region")
    private EventRegion eventRegion;

    @Column(name = "prize")
    private String prize;
}
