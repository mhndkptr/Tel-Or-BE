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
        FaqRequest request = FaqRequest.builder()
                .question("Apa itu Tel-Or?")
                .answer("Tel-Or adalah aplikasi buatan anak Telkom.")
                .category("umum")
                .build();

        FaqEntity entity = faqMapper.toEntity(request);

        assertEquals(request.getQuestion(), entity.getQuestion());
        assertEquals(request.getAnswer(), entity.getAnswer());
        assertEquals(request.getCategory(), entity.getCategory());
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