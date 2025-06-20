package com.pbo.telor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbo.telor.dto.request.UserRequest;
import com.pbo.telor.dto.response.UserResponse;
import com.pbo.telor.model.UserEntity;
import com.pbo.telor.model.UserEntity.Role;
import com.pbo.telor.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
                com.pbo.telor.config.SecurityConfig.class
})
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @MockBean
        private com.pbo.telor.utils.JwtUtil jwtUtil;

        @MockBean
        private com.pbo.telor.security.JwtAuthenticationFilter jwtAuthFilter;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void shouldReturnUserResponse_whenGetById() throws Exception {
                UUID id = UUID.randomUUID();
                UserResponse response = new UserResponse(id, "John Doe", "john@example.com", Role.ADMIN, null);

                Mockito.when(userService.findById(id)).thenReturn(response);

                mockMvc.perform(get("/api/v1/users/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.userId").value(id.toString()))
                                .andExpect(jsonPath("$.data.fullname").value("John Doe"));
        }

        @Test
        void shouldReturnCreatedUser_whenCreateUser() throws Exception {
                UserEntity request = UserEntity.builder()
                                .fullname("John Doe")
                                .email("john@example.com")
                                .password("password123")
                                .role(Role.ADMIN)
                                .build();

                UserResponse response = new UserResponse(UUID.randomUUID(), "John Doe", "john@example.com", Role.ADMIN,
                                null);

                Mockito.when(userService.create(any(UserRequest.class))).thenReturn(response);

                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.fullname").value("John Doe"))
                                .andExpect(jsonPath("$.data.email").value("john@example.com"));
        }

        @Test
        void shouldReturnPagedUsers_whenGetUsersPaged() throws Exception {
                List<UserResponse> userList = List.of(
                                new UserResponse(UUID.randomUUID(), "John Doe", "john@example.com", Role.ADMIN, null));
                Page<UserResponse> userPage = new PageImpl<>(userList, PageRequest.of(0, 10), 1);

                Mockito.when(userService.findAllPaged(anyInt(), anyInt())).thenReturn(userPage);

                mockMvc.perform(get("/api/v1/users?page=0&limit=10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data[0].fullname").value("John Doe"));
        }

        @Test
        void shouldReturnUpdatedUser_whenUpdateUser() throws Exception {
                UUID id = UUID.randomUUID();
                UserEntity request = UserEntity.builder()
                                .fullname("Jane Doe")
                                .email("jane@example.com")
                                .role(Role.ORGANIZER)
                                .build();

                UserResponse response = new UserResponse(id, "Jane Doe", "jane@example.com", Role.ORGANIZER, null);

                Mockito.when(userService.update(eq(id), any(UserEntity.class))).thenReturn(response);

                mockMvc.perform(put("/api/v1/users/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.userId").value(id.toString()))
                                .andExpect(jsonPath("$.data.fullname").value("Jane Doe"));
        }

        @Test
        void shouldReturnPatchedUser_whenPatchUser() throws Exception {
                UUID id = UUID.randomUUID();
                UserEntity patchData = UserEntity.builder()
                                .fullname("Patched Name")
                                .build();

                UserResponse response = new UserResponse(id, "Patched Name", "john@example.com", Role.ADMIN, null);

                Mockito.when(userService.patchUser(eq(id), any(UserEntity.class))).thenReturn(response);

                mockMvc.perform(patch("/api/v1/users/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patchData)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.userId").value(id.toString()))
                                .andExpect(jsonPath("$.data.fullname").value("Patched Name"));
        }

        @Test
        void shouldReturnOk_whenDeleteUser() throws Exception {
                UUID id = UUID.randomUUID();

                Mockito.doNothing().when(userService).delete(id);

                mockMvc.perform(delete("/api/v1/users/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("User deleted successfully"));
        }
}
