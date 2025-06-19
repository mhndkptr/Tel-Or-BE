package com.pbo.telor.model;

import com.pbo.telor.enums.EventRegion;
import com.pbo.telor.enums.EventType;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.EnumType;
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
@Table(name = "events")
public abstract class EventEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID eventId;

    @NotBlank(message = "Event name is required")
    @Column(name = "event_name", nullable = false)
    private String eventName;

    @ElementCollection
    @CollectionTable(name = "event_images", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "image_url")
    private List<String> image;

    @NotBlank(message = "Description is required")
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotBlank(message = "Content is required")
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @NotNull(message = "Start date is required")
    @Column(name = "start_event", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startEvent;

    @NotNull(message = "End date is required")
    @Column(name = "end_event", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endEvent;

    @ManyToOne
    @JoinColumn(name = "ormawa_id")
    private OrmawaEntity ormawa;

    public int getDurationInDays() {
        long diff = endEvent.getTime() - startEvent.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
}
