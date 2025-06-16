package com.pbo.telor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbo.telor.dto.request.AuthRequest;
import com.pbo.telor.service.AuthService;
import com.pbo.telor.service.TokenService;
import com.pbo.telor.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
        com.pbo.telor.config.SecurityConfig.class
})
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AuthService authService;

    @MockBean
    private TokenService tokenService;

    @Test
    void shouldReturnAuthResponse_whenLoginSuccess() throws Exception {
        AuthRequest.LoginRequest request = new AuthRequest.LoginRequest("user@test.com", "pass");
        UserDetails userDetails = new User("user@test.com", "pass", List.of());

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);

        Mockito.when(authManager.authenticate(any())).thenReturn(auth);
        Mockito.when(jwtUtil.generateAccessToken(userDetails)).thenReturn("access-token");
        Mockito.when(jwtUtil.generateRefreshToken(userDetails)).thenReturn("refresh-token");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User successfully logged in"))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"));
    }

    @Test
    void shouldReturnAuthResponse_whenRefreshSuccess() throws Exception {
        AuthRequest.RefreshTokenRequest request = new AuthRequest.RefreshTokenRequest("refresh-token");
        UserDetails userDetails = new User("user@test.com", "pass", List.of());

        Mockito.when(jwtUtil.extractUsernameFromRefreshToken("refresh-token")).thenReturn("user@test.com");
        Mockito.when(authService.loadUserByUsername("user@test.com")).thenReturn(userDetails);
        Mockito.when(tokenService.isTokenBlacklisted("refresh-token")).thenReturn(false);
        Mockito.when(jwtUtil.validateRefreshToken("refresh-token", userDetails)).thenReturn(true);
        Mockito.when(jwtUtil.generateAccessToken(userDetails)).thenReturn("new-access-token");

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Token refreshed successfully"))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"));
    }

    @Test
    void shouldReturnUnauthorized_whenRefreshInvalidToken() throws Exception {
        AuthRequest.RefreshTokenRequest request = new AuthRequest.RefreshTokenRequest("refresh-token");
        UserDetails userDetails = new User("user@test.com", "pass", List.of());

        Mockito.when(jwtUtil.extractUsernameFromRefreshToken("refresh-token")).thenReturn("user@test.com");
        Mockito.when(authService.loadUserByUsername("user@test.com")).thenReturn(userDetails);
        Mockito.when(tokenService.isTokenBlacklisted("refresh-token")).thenReturn(true);

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.error.message").value("Invalid or expired refresh token"));
    }

    @Test
    void shouldLogoutSuccessfully() throws Exception {
        AuthRequest.LogoutRequest request = new AuthRequest.LogoutRequest("refresh-token");

        mockMvc.perform(post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer access-token")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"));

        Mockito.verify(tokenService).blacklistToken("access-token",
                com.pbo.telor.model.TokenBlacklistEntity.TokenType.ACCESS);
        Mockito.verify(tokenService).blacklistToken("refresh-token",
                com.pbo.telor.model.TokenBlacklistEntity.TokenType.REFRESH);
    }
}
