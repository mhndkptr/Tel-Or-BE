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
@Table(name = "event_lombas")
public class EventLomba extends EventEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "stage")
    private EventRegion stage;

    @Column(name = "prize_pool")
    private String prizePool;
}
