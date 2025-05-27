package com.pbo.telor.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {
    public record LoginRequest(
        @NotBlank String email,
        @NotBlank String password
    ) {}

    public record RefreshTokenRequest(
        @NotBlank String refreshToken
    ) {}

    public record LogoutRequest(
        @NotBlank String refreshToken
    ) {}
}

