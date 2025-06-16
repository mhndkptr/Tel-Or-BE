package com.pbo.telor.mapper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import com.pbo.telor.dto.request.EventRequest;
import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.model.EventEntity;



public class EventMapper {

    private static final SimpleDateFormat isoFormatter;
    static {
        isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        isoFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static EventResponse toResponse(EventEntity entity) {
        return EventResponse.builder()
                .eventId(entity.getEventId())
                .eventName(entity.getEventName())
                .image(entity.getImage())
                .description(entity.getDescription())
                .content(entity.getContent())
                .eventType(entity.getEventType())
                .startEvent(entity.getStartEvent() != null ? isoFormatter.format(entity.getStartEvent()) : null)
                .endEvent(entity.getEndEvent() != null ? isoFormatter.format(entity.getEndEvent()) : null)
                .duration(entity.getDurationInDays())
                .build();
    }

    public static void updateEntityFromRequest(EventEntity entity, EventRequest request) {
        if (request.getEventName() != null) entity.setEventName(request.getEventName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getContent() != null) entity.setContent(request.getContent());
        if (request.getEventType() != null) entity.setEventType(request.getEventType());
        if (request.getStartEvent() != null) entity.setStartEvent(request.getStartEvent());
        if (request.getEndEvent() != null) entity.setEndEvent(request.getEndEvent());
    }

    public static void fillEntityFromRequest(EventEntity entity, EventRequest request, List<String> imageUrls) {
        updateEntityFromRequest(entity, request);
        if (imageUrls != null && !imageUrls.isEmpty()) {
            entity.setImage(imageUrls);
        }
    }
}
