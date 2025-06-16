package com.pbo.telor.service;

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

        String folder = "event";
        List<String> urls = uploadService.saveFiles(folder, new MultipartFile[]{file});
        String imageUrl = urls.get(0);

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

        entity.setEventName(request.getEventName());
        entity.setImage(List.of(imageUrl));
        entity.setDescription(request.getDescription());
        entity.setContent(request.getContent());
        entity.setEventType(request.getEventType());
        entity.setStartEvent(request.getStartEvent());
        entity.setEndEvent(request.getEndEvent());

        EventEntity saved = eventRepository.save(entity);
        return EventMapper.toResponse(saved);
    }


    public EventResponse updateEvent(UUID id, EventRequest request) {
        EventEntity entity = eventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found"));

        MultipartFile file = request.getImage();
        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadService.saveFiles("event", new MultipartFile[]{file}).get(0);
            entity.setImage(List.of(imageUrl)); // replace image
        }

        EventMapper.updateEntityFromRequest(entity, request);
        EventEntity updated = eventRepository.save(entity);
        return EventMapper.toResponse(updated);
    }



    public EventResponse patchEvent(UUID id, EventRequest request) {
        EventEntity entity = eventRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Event not found"));

        if (request.getEventName() != null) entity.setEventName(request.getEventName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getContent() != null) entity.setContent(request.getContent());
        if (request.getEventType() != null) entity.setEventType(request.getEventType());
        if (request.getStartEvent() != null) entity.setStartEvent(request.getStartEvent());
        if (request.getEndEvent() != null) entity.setEndEvent(request.getEndEvent());

        MultipartFile file = request.getImage();
        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadService.saveFiles("event", new MultipartFile[]{file}).get(0);
            entity.setImage(List.of(imageUrl)); // replace image
        }

        EventEntity patched = eventRepository.save(entity);
        return EventMapper.toResponse(patched);
    }



    public void deleteEvent(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new NoSuchElementException("Event not found with ID");
        }
        eventRepository.deleteById(id);
    }

    public Page<EventResponse> findAllFiltered(
            int page, int size,
            String keyword,
            EventType type,
            Date startDate,
            Date endDate
    ) {
        Specification<EventEntity> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("eventName")), "%" + keyword.toLowerCase() + "%")
            );
        }
        if (type != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.get("eventType"), type)
            );
        }
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) ->
                cb.and(
                    cb.greaterThanOrEqualTo(root.get("startEvent"), startDate),
                    cb.lessThanOrEqualTo(root.get("endEvent"), endDate)
                )
            );
        }

        return eventRepository.findAll(spec, PageRequest.of(page, size))
                .map(EventMapper::toResponse);
    }
}
