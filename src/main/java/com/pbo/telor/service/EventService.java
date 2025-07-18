package com.pbo.telor.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pbo.telor.dto.request.EventRequest;
import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.enums.EventType;
import com.pbo.telor.exception.BadRequestException;
import com.pbo.telor.mapper.EventMapper;
import com.pbo.telor.model.EventBeasiswa;
import com.pbo.telor.model.EventCompanyVisit;
import com.pbo.telor.model.EventEntity;
import com.pbo.telor.model.EventLomba;
import com.pbo.telor.model.EventOpenRecruitment;
import com.pbo.telor.model.EventSeminar;
import com.pbo.telor.repository.EventRepository;
import com.pbo.telor.model.OrmawaEntity;
import com.pbo.telor.repository.OrmawaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final OrmawaRepository ormawaRepository;
    private final UploadService uploadService;

    public Page<EventResponse> findAllPaged(int page, int size) {
        return eventRepository.findAll(PageRequest.of(page, size))
                .map(EventMapper::toResponse);
    }

    public EventResponse getEventById(UUID id) {
        return eventRepository.findById(id)
                .map(EventMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Event not found"));
    }

    public EventResponse createEvent(EventRequest request) {
        MultipartFile file = request.getImage();

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required when creating an event");
        }

        if (request.getOrmawaId() == null) {
            throw new IllegalArgumentException("OrmawaId is required");
        }

        OrmawaEntity ormawa = ormawaRepository.findById(request.getOrmawaId())
                .orElseThrow(() -> new IllegalArgumentException("Ormawa not found"));

        EventType type = request.getEventType();

        if ((type == EventType.SEMINAR || type == EventType.LOMBA || type == EventType.BEASISWA)
                && request.getEventRegion() == null) {
            throw new IllegalArgumentException("EventRegion is required for SEMINAR, LOMBA, or BEASISWA");
        }

        if ((type == EventType.LOMBA || type == EventType.BEASISWA)
                && (request.getPrize() == null || request.getPrize().isBlank())) {
            throw new IllegalArgumentException("Prize is required for LOMBA or BEASISWA");
        }

        String imageUrl = uploadService.saveFiles("event", new MultipartFile[] { file }).get(0);

        EventEntity entity;
        switch (type) {
            case SEMINAR -> {
                EventSeminar seminar = new EventSeminar();
                seminar.setEventRegion(request.getEventRegion());
                entity = seminar;
            }
            case LOMBA -> {
                EventLomba lomba = new EventLomba();
                lomba.setEventRegion(request.getEventRegion());
                lomba.setPrize(request.getPrize());
                entity = lomba;
            }
            case BEASISWA -> {
                EventBeasiswa beasiswa = new EventBeasiswa();
                beasiswa.setEventRegion(request.getEventRegion());
                beasiswa.setPrize(request.getPrize());
                entity = beasiswa;
            }
            case COMPANY_VISIT -> entity = new EventCompanyVisit();
            case OPEN_RECRUITMENT -> entity = new EventOpenRecruitment();
            default -> throw new IllegalArgumentException("Unsupported event type: " + type);
        }

        entity.setEventName(request.getEventName());
        entity.setImage(List.of(imageUrl));
        entity.setDescription(request.getDescription());
        entity.setContent(request.getContent());
        entity.setEventType(type);
        entity.setStartEvent(request.getStartEvent());
        entity.setEndEvent(request.getEndEvent());
        entity.setOrmawa(ormawa);

        EventEntity saved = eventRepository.save(entity);
        return EventMapper.toResponse(saved);
    }

    public EventResponse updateEvent(UUID id, EventRequest request) {
        EventEntity entity = eventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found"));

        if (request.getEventType() != null) {
            throw new BadRequestException("EventType is not allowed for updating an event");
        }

        if (request.getOrmawaId() != null) {
            OrmawaEntity ormawa = ormawaRepository.findById(request.getOrmawaId())
                    .orElseThrow(() -> new IllegalArgumentException("Ormawa not found"));
            entity.setOrmawa(ormawa);
        }

        // Update image jika ada
        MultipartFile file = request.getImage();
        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadService.saveFiles("event", new MultipartFile[] { file }).get(0);
            entity.setImage(new ArrayList<>(List.of(imageUrl)));
        }

        // Update field umum
        if (request.getEventName() != null)
            entity.setEventName(request.getEventName());
        if (request.getDescription() != null)
            entity.setDescription(request.getDescription());
        if (request.getContent() != null)
            entity.setContent(request.getContent());
        if (request.getEventType() != null)
            entity.setEventType(request.getEventType());
        if (request.getStartEvent() != null)
            entity.setStartEvent(request.getStartEvent());
        if (request.getEndEvent() != null)
            entity.setEndEvent(request.getEndEvent());

        // Update field khusus turunan
        if (entity instanceof EventLomba lomba) {
            if (request.getPrize() != null)
                lomba.setPrize(request.getPrize());
            if (request.getEventRegion() != null)
                lomba.setEventRegion(request.getEventRegion());
        } else if (entity instanceof EventBeasiswa beasiswa) {
            if (request.getPrize() != null)
                beasiswa.setPrize(request.getPrize());
            if (request.getEventRegion() != null)
                beasiswa.setEventRegion(request.getEventRegion());
        } else if (entity instanceof EventSeminar seminar) {
            if (request.getEventRegion() != null)
                seminar.setEventRegion(request.getEventRegion());
        }

        EventEntity updated = eventRepository.save(entity);
        return EventMapper.toResponse(updated);
    }

    public void deleteEvent(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new NoSuchElementException("Event not found with ID");
        }
        eventRepository.deleteById(id);
    }

    public Page<EventResponse> findAllFiltered(
            int page,
            int size,
            String keyword,
            EventType type,
            Date startDate,
            Date endDate,
            UUID ormawaId) {
        Specification<EventEntity> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(
                    (root, query, cb) -> cb.like(cb.lower(root.get("eventName")), "%" + keyword.toLowerCase() + "%"));
        }
        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("eventType"), type));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) -> cb.and(
                    cb.greaterThanOrEqualTo(root.get("startEvent"), startDate),
                    cb.lessThanOrEqualTo(root.get("endEvent"), endDate)));
        }
        if (ormawaId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("ormawa").get("id"), ormawaId));
        }

        return eventRepository.findAll(spec, PageRequest.of(page, size))
                .map(EventMapper::toResponse);
    }

    public List<EventResponse> findAll() {
        return eventRepository.findAll()
                .stream()
                .map(EventMapper::toResponse)
                .toList();
    }
}
