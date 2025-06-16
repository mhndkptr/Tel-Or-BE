package com.pbo.telor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbo.telor.dto.request.EventRequest;
import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.enums.EventType;
import com.pbo.telor.enums.EventRegion;
import com.pbo.telor.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = EventController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
    com.pbo.telor.config.SecurityConfig.class
})
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService; // harus ada, biar nggak ApplicationContext error

    @MockBean
    private com.pbo.telor.utils.JwtUtil jwtUtil;
    @MockBean
    private com.pbo.telor.security.JwtAuthenticationFilter jwtAuthFilter;

    private EventResponse createSampleResponse() {
        return EventResponse.builder()
                .eventId(UUID.randomUUID())
                .eventName("Test Event")
                .image(List.of("img1.jpg"))
                .description("desc")
                .content("content")
                .eventType(EventType.SEMINAR)
                .eventRegion(EventRegion.Regional)
                .startEvent("2025-06-01T10:00:00.000Z")
                .endEvent("2025-06-02T10:00:00.000Z")
                .duration(1)
                .build();
    }

    @Test
    void givenValidRequest_whenGetEventsPaged_thenReturnsPagedEvents() throws Exception {
        EventResponse response = createSampleResponse();
        Page<EventResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

        Mockito.when(eventService.findAllFiltered(anyInt(), anyInt(), any(), any(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/events")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidId_whenGetEventById_thenReturnsEvent() throws Exception {
        UUID id = UUID.randomUUID();
        EventResponse response = createSampleResponse();

        Mockito.when(eventService.getEventById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/events/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidRequest_whenCreateEvent_thenReturnsCreatedEvent() throws Exception {
        EventResponse response = createSampleResponse();

        Mockito.when(eventService.createEvent(any(EventRequest.class))).thenReturn(response);

        MockMultipartFile image = new MockMultipartFile("image", "img.jpg", "image/jpeg", "dummy".getBytes());

        mockMvc.perform(multipart("/api/v1/events")
                        .file(image)
                        .param("eventName", "Test Event")
                        .param("description", "desc")
                        .param("content", "content")
                        .param("eventType", EventType.SEMINAR.name())
                        .param("eventRegion", EventRegion.Regional.name())
                        .param("startEvent", "2025-06-01T10:00:00.000Z")
                        .param("endEvent", "2025-06-02T10:00:00.000Z")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidId_whenDeleteEvent_thenReturnsSuccess() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.doNothing().when(eventService).deleteEvent(id);

        mockMvc.perform(delete("/api/v1/events/{id}", id))
                .andExpect(status().isOk());
    }
}
