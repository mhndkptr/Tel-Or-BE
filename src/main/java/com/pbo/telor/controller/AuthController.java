package com.pbo.telor.controller;

import com.pbo.telor.dto.request.AuthRequest;
import com.pbo.telor.dto.response.AuthResponse;
import com.pbo.telor.service.AuthService;
import com.pbo.telor.service.TokenService;
import com.pbo.telor.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest.LoginRequest request) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String accessToken = JwtUtil.generateAccessToken(user);
        String refreshToken = JwtUtil.generateRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody AuthRequest.RefreshTokenRequest request) {
        String username = JwtUtil.extractUsernameFromRefreshToken(request.refreshToken());
        UserDetails user = authService.loadUserByUsername(username);

        if (tokenService.isTokenBlacklisted(request.refreshToken()) ||
            !JwtUtil.validateRefreshToken(request.refreshToken(), user)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String newAccessToken = JwtUtil.generateAccessToken(user);
        return new AuthResponse(newAccessToken, request.refreshToken());
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authHeader,
                         @RequestBody AuthRequest.LogoutRequest request) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            tokenService.blacklistToken(accessToken, "ACCESS");
        }

        if (request.refreshToken() != null) {
            tokenService.blacklistToken(request.refreshToken(), "REFRESH");
        }

        return "Logged out successfully";
    }
}