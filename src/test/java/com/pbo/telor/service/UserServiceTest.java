package com.pbo.telor.service;

import com.pbo.telor.dto.request.UserRequest;
import com.pbo.telor.dto.response.UserResponse;
import com.pbo.telor.model.UserEntity;
import com.pbo.telor.model.UserEntity.Role;
import com.pbo.telor.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnUserResponse_whenFindByIdAndDataExists() {
        UUID id = UUID.randomUUID();
        UserEntity entity = UserEntity.builder()
                .id(id)
                .fullname("John Doe")
                .email("john@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();
        UserResponse response = new UserResponse(id, "John Doe", "john@example.com", Role.ADMIN, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(entity));

        UserResponse result = userService.findById(id);

        assertEquals(response, result);
        verify(userRepository).findById(id);
    }

    @Test
    void shouldThrowNotFoundException_whenFindByIdAndDataNotExists() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(com.pbo.telor.exception.NotFoundException.class, () -> userService.findById(id));
    }

    @Test
    void shouldSaveUserAndReturnResponse_whenCreateCalled() {
        UserRequest request = new UserRequest(
                "John Doe",
                "john@example.com",
                "password123",
                Role.ADMIN);

        UserEntity savedEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .fullname("John Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role(Role.ADMIN)
                .build();

        UserResponse expectedResponse = new UserResponse(
                savedEntity.getId(),
                "John Doe",
                "john@example.com",
                Role.ADMIN,
                null);

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        UserResponse result = userService.create(request);

        assertEquals(expectedResponse, result);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldUpdateUser_whenUpdateCalledAndUserExists() {
        UUID id = UUID.randomUUID();
        UserEntity existing = UserEntity.builder()
                .id(id)
                .fullname("Old Name")
                .email("old@example.com")
                .role(Role.ORGANIZER)
                .build();
        UserEntity updateData = UserEntity.builder()
                .fullname("New Name")
                .email("new@example.com")
                .role(Role.ADMIN)
                .build();
        UserEntity saved = UserEntity.builder()
                .id(id)
                .fullname("New Name")
                .email("new@example.com")
                .role(Role.ADMIN)
                .build();
        UserResponse response = new UserResponse(id, "New Name", "new@example.com", Role.ADMIN, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(saved);

        UserResponse result = userService.update(id, updateData);

        assertEquals(response, result);
        verify(userRepository).findById(id);
        verify(userRepository).save(existing);
    }

    @Test
    void shouldThrowNoSuchElementException_whenUpdateUserNotFound() {
        UUID id = UUID.randomUUID();
        UserEntity updateData = UserEntity.builder().fullname("X").email("Y").role(Role.ORGANIZER).build();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.update(id, updateData));
    }

    @Test
    void shouldPatchUser_whenPatchUserCalledAndUserExists() {
        UUID id = UUID.randomUUID();
        UserEntity existing = UserEntity.builder()
                .id(id)
                .fullname("Old Name")
                .email("old@example.com")
                .role(Role.ORGANIZER)
                .build();
        UserEntity patchData = UserEntity.builder()
                .fullname("Patched Name")
                .build();
        UserEntity saved = UserEntity.builder()
                .id(id)
                .fullname("Patched Name")
                .email("old@example.com")
                .role(Role.ORGANIZER)
                .build();
        UserResponse response = new UserResponse(id, "Patched Name", "old@example.com", Role.ORGANIZER, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(saved);

        UserResponse result = userService.patchUser(id, patchData);

        assertEquals(response, result);
        verify(userRepository).findById(id);
        verify(userRepository).save(existing);
    }

    @Test
    void shouldThrowNotFoundException_whenPatchUserNotFound() {
        UUID id = UUID.randomUUID();
        UserEntity patchData = UserEntity.builder().fullname("X").build();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(com.pbo.telor.exception.NotFoundException.class, () -> userService.patchUser(id, patchData));
    }

    @Test
    void shouldDeleteUser_whenDataExists() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(true);

        userService.delete(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void shouldThrowNoSuchElementException_whenDeleteAndDataNotExists() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> userService.delete(id));
    }
}
