package com.pbo.telor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbo.telor.dto.response.OrmawaResponse;
import com.pbo.telor.enums.OrmawaCategory;
import com.pbo.telor.mapper.OrmawaMapper;
import com.pbo.telor.repository.OrmawaRepository;
import com.pbo.telor.service.OrmawaService;
import com.pbo.telor.service.UploadService;
import com.pbo.telor.utils.JwtUtil;

@WebMvcTest(value = OrmawaController.class, excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
                com.pbo.telor.config.SecurityConfig.class
})
@AutoConfigureMockMvc(addFilters = false)
class OrmawaControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UploadService uploadService;

        @MockBean
        private OrmawaMapper ormawaMapper;

        @MockBean
        private OrmawaRepository ormawaRepository;

        @MockBean
        private OrmawaService ormawaService;

        @MockBean
        private JwtUtil jwtUtil;

        @MockBean
        private com.pbo.telor.security.JwtAuthenticationFilter jwtAuthFilter;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void getOrmawaById_shouldReturnOrmawa() throws Exception {
                UUID id = UUID.randomUUID();
                OrmawaResponse response = OrmawaResponse.builder()
                                .id(id)
                                .ormawaName("Test Ormawa")
                                .category(OrmawaCategory.COMMUNITY)
                                .build();

                when(ormawaService.getOrmawaById(id)).thenReturn(response);

                mockMvc.perform(get("/api/v1/ormawa/{ormawaId}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.id").value(id.toString()))
                                .andExpect(jsonPath("$.data.ormawaName").value("Test Ormawa"))
                                .andExpect(jsonPath("$.data.category").value("COMMUNITY"));
        }

        @Test
        void createOrmawa_shouldReturnCreatedOrmawa() throws Exception {
                OrmawaResponse response = OrmawaResponse.builder()
                        .id(UUID.randomUUID())
                        .ormawaName("New Ormawa")
                        .category(OrmawaCategory.ORGANIZATION)
                        .build();

                when(ormawaService.createOrmawa(any())).thenReturn(response);

                mockMvc.perform(multipart("/api/v1/ormawa")
                        .file(new MockMultipartFile("icon", "icon.png", "image/png", "dummy".getBytes()))
                        .file(new MockMultipartFile("background", "bg.png", "image/png", "dummy".getBytes()))
                        .param("ormawaName", "New Ormawa")
                        .param("description", "desc")
                        .param("content", "content")
                        .param("isOpenRegistration", "true")
                        .param("category", "ORGANIZATION")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ormawaName").value("New Ormawa"))
                .andExpect(jsonPath("$.data.category").value("ORGANIZATION"));
        }

        @Test
        void updateOrmawa_shouldReturnUpdatedOrmawa() throws Exception {
                UUID id = UUID.randomUUID();
                OrmawaResponse response = OrmawaResponse.builder()
                        .id(id)
                        .ormawaName("Updated Ormawa")
                        .category(OrmawaCategory.ORGANIZATION)
                        .build();

                when(ormawaService.updateOrmawa(eq(id), any())).thenReturn(response);

                mockMvc.perform(multipart("/api/v1/ormawa/{ormawaId}", id)
                        .file(new MockMultipartFile("icon", "icon.png", "image/png", "dummy".getBytes()))
                        .file(new MockMultipartFile("background", "bg.png", "image/png", "dummy".getBytes()))
                        .param("ormawaName", "Updated Ormawa")
                        .param("category", "ORGANIZATION")
                        .with(request -> { request.setMethod("PUT"); return request; }) // agar multipart jadi PUT
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.ormawaName").value("Updated Ormawa"))
                .andExpect(jsonPath("$.data.category").value("ORGANIZATION"));
        }

        @Test
        void patchOrmawa_shouldReturnPatchedOrmawa() throws Exception {
                UUID id = UUID.randomUUID();
                OrmawaResponse response = OrmawaResponse.builder()
                        .id(id)
                        .ormawaName("Patched Ormawa")
                        .category(OrmawaCategory.COMMUNITY)
                        .build();

                when(ormawaService.patchOrmawa(eq(id), any())).thenReturn(response);

                mockMvc.perform(multipart("/api/v1/ormawa/{ormawaId}", id)
                        .file(new MockMultipartFile("icon", "icon.png", "image/png", "dummy".getBytes()))
                        .file(new MockMultipartFile("background", "bg.png", "image/png", "dummy".getBytes()))
                        .param("ormawaName", "Patched Ormawa")
                        .param("category", "COMMUNITY")
                        .with(request -> { request.setMethod("PATCH"); return request; }) // agar multipart jadi PATCH
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.ormawaName").value("Patched Ormawa"))
                .andExpect(jsonPath("$.data.category").value("COMMUNITY"));
        }

        @Test
        void deleteOrmawa_shouldReturnSuccessMessage() throws Exception {
                UUID id = UUID.randomUUID();

                mockMvc.perform(delete("/api/v1/ormawa/{ormawaId}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Successfully deleted Ormawa"));

                verify(ormawaService).deleteOrmawa(id);
        }

}