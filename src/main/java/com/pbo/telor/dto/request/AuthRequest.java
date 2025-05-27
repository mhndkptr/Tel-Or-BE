package com.pbo.telor.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {
    public record LoginRequest(
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Password is required") String password
    ) {}

    public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token is required") String refreshToken
    ) {}

    public record LogoutRequest(
        @NotBlank(message = "Refresh token is required") String refreshToken
    ) {}
}
