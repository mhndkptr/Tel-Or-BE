package com.pbo.telor.mapper;

import com.pbo.telor.dto.request.FaqRequest;
import com.pbo.telor.dto.response.FaqResponse;
import com.pbo.telor.model.FaqEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FaqMapperTest {

    private final FaqMapper faqMapper = new FaqMapper();

    @Test
    void toEntity_shouldMapRequestToEntity() {
        FaqRequest request = new FaqRequest(
                "Apa itu Tel-Or?",
                "Tel-Or adalah aplikasi buatan anak Telkom.",
                "umum");

        FaqEntity entity = faqMapper.toEntity(request);

        assertEquals(request.question(), entity.getQuestion());
        assertEquals(request.answer(), entity.getAnswer());
        assertEquals(request.category(), entity.getCategory());
    }

    @Test
    void toResponse_shouldMapEntityToResponse() {
        UUID id = UUID.randomUUID();
        FaqEntity entity = FaqEntity.builder()
                .id(id)
                .question("Apa itu Tel-Or?")
                .answer("Tel-Or adalah aplikasi buatan anak Telkom.")
                .category("umum")
                .build();

        FaqResponse response = faqMapper.toResponse(entity);

        assertEquals(entity.getId(), response.getId());
        assertEquals(entity.getQuestion(), response.getQuestion());
        assertEquals(entity.getAnswer(), response.getAnswer());
        assertEquals(entity.getCategory(), response.getCategory());
    }
}