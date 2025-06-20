package com.pbo.telor.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pbo.telor.model.UserEntity.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
                UUID userId,
                String fullname,
                String email,
                Role role,
                OrmawaResponse ormawa) {
}