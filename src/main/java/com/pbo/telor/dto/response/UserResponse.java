package com.pbo.telor.dto.response;

import java.util.UUID;
import com.pbo.telor.model.UserEntity.Role;

public record UserResponse(
        UUID userId,
        String fullname,
        String email,
        Role role) {
}