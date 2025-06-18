package com.pbo.telor.service;

import com.pbo.telor.model.UserEntity;
import com.pbo.telor.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnUserDetails_whenUserExists() {
        UserEntity user = UserEntity.builder()
                .email("user@test.com")
                .password("pass")
                .role(UserEntity.Role.ADMIN)
                .build();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = authService.loadUserByUsername("user@test.com");

        assertEquals("user@test.com", userDetails.getUsername());
        assertEquals("pass", userDetails.getPassword());

        System.out.println("Authorities: " + userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void shouldThrowException_whenUserNotFound() {
        when(userRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername("notfound@test.com"));
    }

    @Test
    void shouldCallTokenService_whenCheckBlacklist() {
        when(tokenService.isTokenBlacklisted("token")).thenReturn(true);

        boolean result = authService.isTokenBlacklisted("token");

        assertTrue(result);
        verify(tokenService).isTokenBlacklisted("token");
    }
}
