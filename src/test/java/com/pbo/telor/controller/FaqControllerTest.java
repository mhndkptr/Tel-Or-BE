package com.pbo.telor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbo.telor.dto.request.FaqRequest;
import com.pbo.telor.dto.response.FaqResponse;
import com.pbo.telor.service.FaqService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = FaqController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
        com.pbo.telor.config.SecurityConfig.class
})
@AutoConfigureMockMvc(addFilters = false)
public class FaqControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FaqService faqService;

    @MockBean
    private com.pbo.telor.utils.JwtUtil jwtUtil;

    @MockBean
    private com.pbo.telor.security.JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnFaqResponse_whenGetFaqById() throws Exception {
        UUID id = UUID.randomUUID();
        FaqResponse response = FaqResponse.builder()
                .id(id)
                .question("Apa itu Tel-Or?")
                .answer("Tel-Or adalah aplikasi buatan anak Telkom.")
                .category("umum")
                .build();

        Mockito.when(faqService.getFaqById(id)).thenReturn(response);

        MvcResult result = mockMvc.perform(get("/api/v1/faqs/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("Response: " + result.getResponse().getContentAsString());

        mockMvc.perform(get("/api/v1/faqs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.question").value("Apa itu Tel-Or?"));
    }

    @Test
    void shouldReturnCreatedFaq_whenCreateFaq() throws Exception {
        FaqRequest request = new FaqRequest(
                "Apa itu Tel-Or?",
                "Tel-Or adalah aplikasi buatan anak Telkom.",
                "umum");

        FaqResponse response = FaqResponse.builder()
                .id(UUID.randomUUID())
                .question(request.question())
                .answer(request.answer())
                .category(request.category())
                .build();

        Mockito.when(faqService.createFaq(any(FaqRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/faqs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(response.getId().toString()))
                .andExpect(jsonPath("$.data.question").value(request.question()));
    }

    @Test
    void shouldReturnPagedFaqs_whenGetFaqsPaged() throws Exception {
        List<FaqResponse> faqList = List.of(
                FaqResponse.builder()
                        .id(UUID.randomUUID())
                        .question("Apa itu Tel-Or?")
                        .answer("Tel-Or adalah aplikasi buatan anak Telkom.")
                        .category("umum")
                        .build());
        Page<FaqResponse> faqPage = new PageImpl<>(faqList, PageRequest.of(0, 10), 1);

        Mockito.when(faqService.findAllPaged(anyInt(), anyInt(), any(), any())).thenReturn(faqPage);

        MvcResult result = mockMvc.perform(get("/api/v1/faqs?page=0&limit=10"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("Response: " + result.getResponse().getContentAsString());

        mockMvc.perform(get("/api/v1/faqs?page=0&limit=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].question").value("Apa itu Tel-Or?"));
    }

    @Test
    void shouldReturnUpdatedFaq_whenUpdateFaq() throws Exception {
        UUID id = UUID.randomUUID();
        FaqRequest request = new FaqRequest(
                "Update Q",
                "Update A",
                "umum");

        FaqResponse response = FaqResponse.builder()
                .id(id)
                .question(request.question())
                .answer(request.answer())
                .category(request.category())
                .build();

        Mockito.when(faqService.updateFaq(eq(id), any(FaqRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/faqs/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.question").value("Update Q"));
    }
}