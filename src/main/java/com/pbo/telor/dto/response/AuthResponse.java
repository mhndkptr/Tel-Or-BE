package com.pbo.telor.dto.response;

public record AuthResponse(
    String accessToken,
    String refreshToken
) {}