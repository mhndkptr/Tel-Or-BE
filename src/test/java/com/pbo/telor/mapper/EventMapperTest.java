package com.pbo.telor.mapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.pbo.telor.dto.request.EventRequest;
import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.enums.EventType;
import com.pbo.telor.model.EventSeminar;

class EventMapperTest {
    @Test
    void toResponse_shouldMapEntityToResponseCorrectly() {
        EventSeminar eventEntity = new EventSeminar();
        eventEntity.setEventName("Test Event");
        eventEntity.setImage(List.of("img1.jpg", "img2.jpg"));
        eventEntity.setDescription("Description");
        eventEntity.setContent("Content");
        eventEntity.setEventType(EventType.SEMINAR);

        Calendar startCal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        startCal.set(2025, Calendar.JUNE, 1, 10, 0, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        Date start = startCal.getTime();

        Calendar endCal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        endCal.set(2025, Calendar.JUNE, 3, 10, 0, 0);
        endCal.set(Calendar.MILLISECOND, 0);
        Date end = endCal.getTime();

        eventEntity.setStartEvent(start);
        eventEntity.setEndEvent(end);

        EventResponse response = EventMapper.toResponse(eventEntity);
        assertNotNull(response);
        assertEquals(eventEntity.getEventName(), response.getEventName());
        assertEquals(eventEntity.getImage(), response.getImage());
        assertEquals(eventEntity.getDescription(), response.getDescription());
        assertEquals(eventEntity.getContent(), response.getContent());
        assertEquals(eventEntity.getEventType(), response.getEventType());
        assertEquals("2025-06-01T10:00:00.000Z", response.getStartEvent());
        assertEquals("2025-06-03T10:00:00.000Z", response.getEndEvent());
    }

    @Test
    void updateEntityFromRequest_shouldUpdateEntityFields() {
        EventSeminar entity = new EventSeminar();
        entity.setEventName("Old Name");
        entity.setDescription("Old Desc");
        entity.setContent("Old Content");
        Date start = new Date();
        Date end = new Date();
        entity.setStartEvent(start);
        entity.setEndEvent(end);

        EventRequest eventRequest = EventRequest.builder()
                .eventName("Updated Event")
                .description("Updated Description")
                .content("Updated Content")
                .startEvent(start)
                .endEvent(end)
                .build();

        EventMapper.updateEntityFromRequest(entity, eventRequest);
        assertEquals(eventRequest.getEventName(), entity.getEventName());
        assertEquals(eventRequest.getDescription(), entity.getDescription());
        assertEquals(eventRequest.getContent(), entity.getContent());
        assertEquals(eventRequest.getStartEvent(), entity.getStartEvent());
        assertEquals(eventRequest.getEndEvent(), entity.getEndEvent());
    }

    @Test
    void fillEntityFromRequest_shouldSetImagesAndUpdateFields() {
        EventSeminar entity = new EventSeminar();
        entity.setImage(new ArrayList<>());
        Date start = new Date();
        Date end = new Date();
        EventRequest eventRequest = EventRequest.builder()
                .eventName("Updated Event")
                .description("Updated Description")
                .content("Updated Content")
                .startEvent(start)
                .endEvent(end)
                .build();
        List<String> imageUrls = new ArrayList<>(List.of("img3.jpg", "img4.jpg"));
        EventMapper.fillEntityFromRequest(entity, eventRequest, imageUrls);
        assertEquals(imageUrls, entity.getImage());
        assertEquals(eventRequest.getEventName(), entity.getEventName());
        assertEquals(eventRequest.getDescription(), entity.getDescription());
        assertEquals(eventRequest.getContent(), entity.getContent());
        assertEquals(eventRequest.getStartEvent(), entity.getStartEvent());
        assertEquals(eventRequest.getEndEvent(), entity.getEndEvent());
    }
}
