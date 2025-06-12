package com.pbo.telor.mapper;

import com.pbo.telor.dto.request.EventRequest;
import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.model.EventEntity;

public class EventMapper {

    public static EventResponse toResponse(EventEntity entity) {
        return EventResponse.builder()
                .eventId(entity.getEventId())
                .eventName(entity.getEventName())
                .image(entity.getImage())
                .description(entity.getDescription())
                .content(entity.getContent())
                .eventType(entity.getEventType())
                .startEvent(entity.getStartEvent())
                .endEvent(entity.getEndEvent())
                .duration(entity.getDurationInDays())
                .build();
    }

    public static void updateEntityFromRequest(EventEntity entity, EventRequest request) {
        entity.setEventName(request.getEventName());
        entity.setImage(request.getImage());
        entity.setDescription(request.getDescription());
        entity.setContent(request.getContent());
        entity.setEventType(request.getEventType()); // enum to enum
        entity.setStartEvent(request.getStartEvent());
        entity.setEndEvent(request.getEndEvent());
    }

    public static void fillEntityFromRequest(EventEntity entity, EventRequest request) {
        updateEntityFromRequest(entity, request);
    }
}
