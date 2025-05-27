package com.pbo.telor.controller;

import com.pbo.telor.dto.common.BaseResponse;
import com.pbo.telor.dto.request.AuthRequest;
import com.pbo.telor.dto.response.AuthResponse;
import com.pbo.telor.model.TokenBlacklistEntity.TokenType;
import com.pbo.telor.service.AuthService;
import com.pbo.telor.service.TokenService;
import com.pbo.telor.utils.JwtUtil;
import com.pbo.telor.utils.ResponseUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest.LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        AuthResponse response = new AuthResponse(accessToken, refreshToken);
        return ResponseUtil.ok(response, "User successfully logged in");
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<AuthResponse>> refresh(@RequestBody AuthRequest.RefreshTokenRequest request) {
        String username = jwtUtil.extractUsernameFromRefreshToken(request.refreshToken());
        UserDetails user = authService.loadUserByUsername(username);

        if (tokenService.isTokenBlacklisted(request.refreshToken()) ||
            !jwtUtil.validateRefreshToken(request.refreshToken(), user)) {
            return ResponseUtil.error(org.springframework.http.HttpStatus.UNAUTHORIZED, "TOKEN_INVALID", "Invalid or expired refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user);
        AuthResponse response = new AuthResponse(newAccessToken, request.refreshToken());
        return ResponseUtil.ok(response, "Token refreshed successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<AuthResponse>> logout(@RequestHeader("Authorization") String authHeader,
                         @RequestBody AuthRequest.LogoutRequest request) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            tokenService.blacklistToken(accessToken, TokenType.ACCESS);
        }

        if (request.refreshToken() != null) {
            tokenService.blacklistToken(request.refreshToken(), TokenType.REFRESH);
        }

        return ResponseUtil.ok(null, "Logged out successfully");
    }
}