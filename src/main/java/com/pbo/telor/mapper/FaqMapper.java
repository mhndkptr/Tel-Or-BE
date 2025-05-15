package com.pbo.telor.mapper;

import com.pbo.telor.dto.request.FaqRequest;
import com.pbo.telor.dto.response.FaqResponse;
import com.pbo.telor.model.FaqEntity;
import org.springframework.stereotype.Component;

@Component
public class FaqMapper {
    public FaqEntity toEntity(FaqRequest request) {
        return FaqEntity.builder()
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .build();
    }

    public FaqResponse toResponse(FaqEntity entity) {
        return FaqResponse.builder()
                .id(entity.getId())
                .question(entity.getQuestion())
                .answer(entity.getAnswer())
                .build();
    }
} 