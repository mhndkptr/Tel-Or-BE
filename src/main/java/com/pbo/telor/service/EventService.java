package com.pbo.telor.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pbo.telor.dto.request.EventRequest;
import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.enums.EventType;
import com.pbo.telor.exception.NotFoundException;
import com.pbo.telor.mapper.EventMapper;
import com.pbo.telor.model.EventBeasiswa;
import com.pbo.telor.model.EventCompanyVisit;
import com.pbo.telor.model.EventEntity;
import com.pbo.telor.model.EventLomba;
import com.pbo.telor.model.EventOpenRecruitment;
import com.pbo.telor.model.EventSeminar;
import com.pbo.telor.repository.EventRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Page<EventResponse> findAllPaged(int page, int size) {
        return eventRepository.findAll(PageRequest.of(page, size))
                .map(EventMapper::toResponse);
    }

    public EventResponse getEventById(UUID id) {
        EventEntity entity = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with ID: " + id));
        return EventMapper.toResponse(entity);
    }

    public EventResponse createEvent(EventRequest request) {
    EventEntity entity;

    EventType type = request.getEventType();

    switch (type) {
        case SEMINAR -> entity = new EventSeminar();
        case LOMBA -> entity = new EventLomba();
        case BEASISWA -> entity = new EventBeasiswa();
        case COMPANY_VISIT -> entity = new EventCompanyVisit();
        case OPEN_RECRUITMENT -> entity = new EventOpenRecruitment();
        default -> throw new IllegalArgumentException("Unsupported event type: " + type);
    }

    // Common field setter
    entity.setEventName(request.getEventName());
    entity.setImage(request.getImage());
    entity.setDescription(request.getDescription());
    entity.setContent(request.getContent());
    entity.setEventType(request.getEventType()); // simpan enum ke entity
    entity.setStartEvent(request.getStartEvent());
    entity.setEndEvent(request.getEndEvent());

    EventEntity saved = eventRepository.save(entity);
    return EventMapper.toResponse(saved);
}


    public EventResponse updateEvent(UUID id, EventRequest request) {
        EventEntity entity = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with ID: " + id));
        EventMapper.updateEntityFromRequest(entity, request);
        EventEntity updated = eventRepository.save(entity);
        return EventMapper.toResponse(updated);
    }

    public EventResponse patchEvent(UUID id, EventRequest request) {
        EventEntity entity = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with ID: " + id));

        if (request.getEventName() != null) entity.setEventName(request.getEventName());
        if (request.getImage() != null) entity.setImage(request.getImage());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getContent() != null) entity.setContent(request.getContent());
        if (request.getEventType() != null) entity.setEventType(request.getEventType());
        if (request.getStartEvent() != null) entity.setStartEvent(request.getStartEvent());
        if (request.getEndEvent() != null) entity.setEndEvent(request.getEndEvent());

        EventEntity patched = eventRepository.save(entity);
        return EventMapper.toResponse(patched);
    }

    public void deleteEvent(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Event not found with ID: " + id);
        }
        eventRepository.deleteById(id);
    }
}
