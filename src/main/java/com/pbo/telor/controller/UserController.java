package com.pbo.telor.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.pbo.telor.dto.common.BaseResponse;
import com.pbo.telor.dto.common.PaginationResponse;
import com.pbo.telor.dto.response.UserResponse;
import com.pbo.telor.model.UserEntity;
import com.pbo.telor.service.UserService;
import com.pbo.telor.utils.ResponseUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.*;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<List<UserResponse>>> getUsersPaged(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        Page<UserResponse> users = userService.findAllPaged(page, limit);

        return ResponseUtil.paged(
                users.getContent(),
                PaginationResponse.builder()
                        .currentPage(users.getNumber() + 1)
                        .totalPage(users.getTotalPages())
                        .totalItem(users.getTotalElements())
                        .limit(users.getSize())
                        .build(),
                "Paged users fetched successfully"
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<UserResponse>> getById(@PathVariable UUID id) {
        UserResponse user = userService.findById(id);
        return ResponseUtil.ok(user, "User found");
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<UserResponse>> create(@Valid @RequestBody UserEntity user) {
        UserResponse created = userService.create(user);
        return ResponseUtil.ok(created, "User created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UserResponse>> update(
            @Valid
            @PathVariable UUID id,
            @RequestBody  UserEntity userData) {
        UserResponse updated = userService.update(id, userData);
        return ResponseUtil.ok(updated, "User updated successfully");
    }
    
    @PatchMapping(value = "/{id}")
    public ResponseEntity<BaseResponse<UserResponse>> patchUser(
            @PathVariable UUID id,
            @RequestBody UserEntity patchData) {

        UserResponse updated = userService.patchUser(id, patchData);
        return ResponseUtil.ok(updated, "User updated successfully");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseUtil.ok(null, "User deleted successfully");
    }
}
