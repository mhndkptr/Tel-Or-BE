package com.pbo.telor.mapper;

import com.pbo.telor.dto.response.UserResponse;
import com.pbo.telor.model.UserEntity;
import com.pbo.telor.model.UserEntity.Role;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    @Test
    void toResponse_shouldMapEntityToResponse() {
        UUID id = UUID.randomUUID();
        UserEntity user = UserEntity.builder()
                .id(id)
                .fullname("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .build();

        UserResponse response = UserMapper.toResponse(user);

        assertEquals(user.getId(), response.userId());
        assertEquals(user.getFullname(), response.fullname());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getRole(), response.role());
    }
}
