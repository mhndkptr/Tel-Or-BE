package com.pbo.telor.dto.request;

import com.pbo.telor.model.UserEntity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @NotBlank(message = "Full name is required")
    String fullname,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @Size(min = 8, message = "Password must be at least 8 characters")
    String password, // optional untuk update, wajib untuk create

    @NotNull(message = "Role is required")
    Role role
) {}
