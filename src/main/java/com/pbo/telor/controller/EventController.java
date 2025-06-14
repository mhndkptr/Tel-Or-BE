package com.pbo.telor.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pbo.telor.dto.common.BaseResponse;
import com.pbo.telor.dto.common.PaginationResponse;
import com.pbo.telor.dto.request.EventRequest;
import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.service.EventService;
import com.pbo.telor.utils.ResponseUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Event")
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<EventResponse>>> getEventsPaged(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        Page<EventResponse> events = eventService.findAllPaged(page, limit);

        return ResponseUtil.paged(
                events.getContent(),
                PaginationResponse.builder()
                        .currentPage(events.getNumber() + 1)
                        .totalPage(events.getTotalPages())
                        .totalItem(events.getTotalElements())
                        .limit(events.getSize())
                        .build(),
                "Paged Events fetched successfully"
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<EventResponse>> getEventById(@PathVariable UUID id) {
        EventResponse event = eventService.getEventById(id);
        return ResponseUtil.ok(event, "Successfully retrieved event");
    }

    @PostMapping
    public ResponseEntity<BaseResponse<EventResponse>> createEvent(@RequestBody EventRequest request) {
        EventResponse event = eventService.createEvent(request);
        return ResponseUtil.ok(event, "Successfully created event");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<EventResponse>> updateEvent(
            @PathVariable UUID id,
            @RequestBody EventRequest request) {
        EventResponse event = eventService.updateEvent(id, request);
        return ResponseUtil.ok(event, "Successfully updated event");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<EventResponse>> patchEvent(
            @PathVariable UUID id,
            @RequestBody EventRequest patchData) {
        EventResponse event = eventService.patchEvent(id, patchData);
        return ResponseUtil.ok(event, "Successfully patched event");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
        return ResponseUtil.ok(null, "Successfully deleted event");
    }
}
