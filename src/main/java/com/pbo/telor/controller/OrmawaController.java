package com.pbo.telor.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pbo.telor.dto.common.BaseResponse;
import com.pbo.telor.dto.common.PaginationResponse;
import com.pbo.telor.dto.request.OrmawaRequest;
import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.dto.response.OrmawaResponse;
import com.pbo.telor.model.EventEntity;
import com.pbo.telor.service.OrmawaService;
import com.pbo.telor.utils.ResponseUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Ormawa")
@RestController
@RequestMapping("/api/v1/ormawa")
@RequiredArgsConstructor
public class OrmawaController {

    private final OrmawaService ormawaService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<OrmawaResponse>>> getOrmawaPaged(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        Page<OrmawaResponse> result = ormawaService.findAllPaged(page, limit);

        return ResponseUtil.paged(
                result.getContent(),
                PaginationResponse.builder()
                        .currentPage(result.getNumber() + 1)
                        .totalPage(result.getTotalPages())
                        .totalItem(result.getTotalElements())
                        .limit(result.getSize())
                        .build(),
                "Paged Ormawa fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OrmawaResponse>> getOrmawaById(@PathVariable UUID id) {
        OrmawaResponse data = ormawaService.getOrmawaById(id);
        return ResponseUtil.ok(data, "Successfully retrieved Ormawa");
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<BaseResponse<List<EventResponse>>> getEventsByOrmawaId(@PathVariable UUID id) {
        OrmawaResponse data = ormawaService.getOrmawaById(id);
        return ResponseUtil.ok(data.getEvents(), "Successfully retrieved Ormawa Events");
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<BaseResponse<OrmawaResponse>> createOrmawa(@ModelAttribute OrmawaRequest request) {
        OrmawaResponse data = ormawaService.createOrmawa(request);
        return ResponseUtil.ok(data, "Successfully created Ormawa");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<BaseResponse<OrmawaResponse>> updateOrmawa(
            @PathVariable UUID id,
            @ModelAttribute OrmawaRequest request) {
        OrmawaResponse data = ormawaService.updateOrmawa(id, request);
        return ResponseUtil.ok(data, "Successfully updated Ormawa");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<BaseResponse<OrmawaResponse>> patchOrmawa(
            @PathVariable UUID id,
            @ModelAttribute OrmawaRequest patchData) {
        OrmawaResponse data = ormawaService.patchOrmawa(id, patchData);
        return ResponseUtil.ok(data, "Successfully patched Ormawa");
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> deleteOrmawa(@PathVariable UUID id) {
        ormawaService.deleteOrmawa(id);
        return ResponseUtil.ok(null, "Successfully deleted Ormawa");
    }
}
