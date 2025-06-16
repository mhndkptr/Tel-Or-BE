package com.pbo.telor.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.pbo.telor.dto.request.EventRequest;
import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.enums.EventRegion;
import com.pbo.telor.enums.EventType;
import com.pbo.telor.model.EventEntity;
import com.pbo.telor.model.EventSeminar;
import com.pbo.telor.repository.EventRepository;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UploadService uploadService;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private EventSeminar createSampleEvent() {
        EventSeminar event = new EventSeminar();
        event.setEventName("Test Event");
        event.setStartEvent(new Date());
        event.setEndEvent(new Date(System.currentTimeMillis() + 86400000)); // +1 hari
        event.setImage(List.of("img1.jpg"));
        event.setDescription("desc");
        event.setContent("content");
        event.setEventType(EventType.SEMINAR);
        return event;
    }

    @Test
    void givenValidPageRequest_whenFindAllPaged_thenReturnsPagedEventResponses() {
        EventSeminar event = createSampleEvent();
        Page<EventEntity> page = new PageImpl<>(List.of(event));
        when(eventRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<EventResponse> result = eventService.findAllPaged(0, 10);

        assertEquals(1, result.getTotalElements());
        verify(eventRepository).findAll(any(PageRequest.class));
    }

    @Test
    void givenExistingId_whenGetEventById_thenReturnsEventResponse() {
        UUID id = UUID.randomUUID();
        EventSeminar event = createSampleEvent();
        when(eventRepository.findById(id)).thenReturn(Optional.of(event));

        EventResponse result = eventService.getEventById(id);

        assertNotNull(result);
        verify(eventRepository).findById(id);
    }

    @Test
    void givenNonExistingId_whenGetEventById_thenThrowsException() {
        UUID id = UUID.randomUUID();
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> eventService.getEventById(id));
    }

    @Test
    void givenValidRequest_whenCreateEvent_thenSavesAndReturnsEventResponse() {
        EventRequest request = mock(EventRequest.class);
        org.springframework.web.multipart.MultipartFile file = mock(org.springframework.web.multipart.MultipartFile.class);

        when(request.getImage()).thenReturn(file);
        when(file.isEmpty()).thenReturn(false);
        when(request.getEventType()).thenReturn(EventType.SEMINAR);
        when(request.getPrize()).thenReturn(null);
        when(request.getEventRegion()).thenReturn(EventRegion.National);
        when(request.getEventName()).thenReturn("Event");
        when(request.getDescription()).thenReturn("Desc");
        when(request.getContent()).thenReturn("Content");
        when(request.getStartEvent()).thenReturn(new Date());
        when(request.getEndEvent()).thenReturn(new Date(System.currentTimeMillis() + 86400000));

        when(uploadService.saveFiles(anyString(), any())).thenReturn(List.of("image.jpg"));
        when(eventRepository.save(any(EventEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        EventResponse response = eventService.createEvent(request);

        assertNotNull(response);
        verify(eventRepository).save(any(EventEntity.class));
    }

    @Test
    void givenValidIdAndRequest_whenUpdateEvent_thenUpdatesAndReturnsEventResponse() {
        UUID id = UUID.randomUUID();
        EventSeminar event = createSampleEvent();
        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(EventEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        EventRequest request = mock(EventRequest.class);
        when(request.getImage()).thenReturn(null);
        when(request.getEventName()).thenReturn("Seminar Hilmi");
        when(request.getDescription()).thenReturn("Hilmi Ngajar NextJS");
        when(request.getContent()).thenReturn("Hilmi Jago");
        when(request.getEventType()).thenReturn(EventType.SEMINAR);
        when(request.getStartEvent()).thenReturn(new Date());
        when(request.getEndEvent()).thenReturn(new Date(System.currentTimeMillis() + 86400000));
        when(request.getEventRegion()).thenReturn(EventRegion.Regional);

        EventResponse response = eventService.updateEvent(id, request);

        assertNotNull(response);
        verify(eventRepository).save(any(EventEntity.class));
    }

    @Test
    void givenValidIdAndRequest_whenPatchEvent_thenPatchesAndReturnsEventResponse() {
        UUID id = UUID.randomUUID();
        EventSeminar event = createSampleEvent();
        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(EventEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        EventRequest request = mock(EventRequest.class);
        when(request.getEventName()).thenReturn("Patched Name");
        when(request.getDescription()).thenReturn("Patched Desc");
        when(request.getContent()).thenReturn("Patched Content");
        when(request.getEventType()).thenReturn(EventType.SEMINAR);
        when(request.getStartEvent()).thenReturn(new Date());
        when(request.getEndEvent()).thenReturn(new Date(System.currentTimeMillis() + 86400000));
        when(request.getEventRegion()).thenReturn(EventRegion.International);
        when(request.getImage()).thenReturn(null);

        EventResponse response = eventService.patchEvent(id, request);

        assertNotNull(response);
        verify(eventRepository).save(any(EventEntity.class));
    }

    @Test
    void givenExistingId_whenDeleteEvent_thenDeletesEvent() {
        UUID id = UUID.randomUUID();
        when(eventRepository.existsById(id)).thenReturn(true);

        eventService.deleteEvent(id);

        verify(eventRepository).deleteById(id);
    }

    @Test
    void givenNonExistingId_whenDeleteEvent_thenThrowsException() {
        UUID id = UUID.randomUUID();
        when(eventRepository.existsById(id)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> eventService.deleteEvent(id));
    }
}