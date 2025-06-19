package com.pbo.telor.mapper;

import org.springframework.stereotype.Component;

import com.pbo.telor.dto.request.OrmawaRequest;
import com.pbo.telor.dto.response.OrmawaResponse;
import com.pbo.telor.model.OrmawaCommunityEntity;
import com.pbo.telor.model.OrmawaEntity;
import com.pbo.telor.model.OrmawaLaboratoryEntity;
import com.pbo.telor.model.OrmawaOrganizationEntity;
import com.pbo.telor.model.OrmawaUKMEntity;

@Component
public class OrmawaMapper {

    // === REQUEST → ENTITY (Create) ===
    public OrmawaEntity fillEntityFromRequest(OrmawaRequest request, String iconUrl, String bgUrl) {
        if (request.getCategory() == null) {
            throw new IllegalArgumentException("Ormawa category cannot be null");
        }

        OrmawaEntity entity;
        switch (request.getCategory()) {
            case COMMUNITY -> entity = new OrmawaCommunityEntity();
            case ORGANIZATION -> entity = new OrmawaOrganizationEntity();
            case LAB -> {
                OrmawaLaboratoryEntity lab = new OrmawaLaboratoryEntity();
                if (request.getLabType() == null) {
                    throw new IllegalArgumentException("Lab type must be filled if category is LAB");
                }
                lab.setLabType(request.getLabType());
                entity = lab;
            }
            case UKM -> {
                OrmawaUKMEntity ukm = new OrmawaUKMEntity();
                if (request.getUkmCategory() == null || request.getUkmCategory().isBlank()) {
                    throw new IllegalArgumentException("UKM Category must be filled if category is UKM");
                }
                ukm.setUkmCategory(request.getUkmCategory());
                entity = ukm;
            }
            default -> throw new IllegalArgumentException("Invalid Ormawa category: " + request.getCategory());
        }

        // set common fields
        entity.setCategory(request.getCategory());
        entity.setOrmawaName(request.getOrmawaName());
        entity.setDescription(request.getDescription());
        entity.setContent(request.getContent());
        entity.setIsOpenRegistration(request.getIsOpenRegistration());
        entity.setIcon(iconUrl);
        entity.setBackground(bgUrl);
        // entity.setEvents(request.getEvents());
        return entity;
    }

    // === ENTITY → RESPONSE ===
    public static OrmawaResponse toResponse(OrmawaEntity entity) {
        OrmawaResponse.OrmawaResponseBuilder builder = OrmawaResponse.builder()
                .id(entity.getId())
                .ormawaName(entity.getOrmawaName())
                .description(entity.getDescription())
                .content(entity.getContent())
                .isOpenRegistration(entity.getIsOpenRegistration())
                .icon(entity.getIcon())
                .background(entity.getBackground())
                .category(entity.getCategory())
                .events(entity.getEvents());

        if (entity instanceof OrmawaLaboratoryEntity lab) {
            builder.labType(lab.getLabType());
        }

        if (entity instanceof OrmawaUKMEntity ukm) {
            builder.ukmCategory(ukm.getUkmCategory());
        }

        return builder.build();
    }

    // === REQUEST → EXISTING ENTITY (Update) ===
    public static void updateEntityFromRequest(OrmawaEntity entity, OrmawaRequest request, String iconUrl,
            String bgUrl) {
        entity.setOrmawaName(request.getOrmawaName());
        entity.setDescription(request.getDescription());
        entity.setContent(request.getContent());
        entity.setIsOpenRegistration(request.getIsOpenRegistration());

        if (iconUrl != null) {
            entity.setIcon(iconUrl);
        }

        if (bgUrl != null) {
            entity.setBackground(bgUrl);
        }

        if (entity instanceof OrmawaLaboratoryEntity lab && request.getLabType() != null) {
            lab.setLabType(request.getLabType());
        }

        if (entity instanceof OrmawaUKMEntity ukm && request.getUkmCategory() != null) {
            ukm.setUkmCategory(request.getUkmCategory());
        }
        // if (request.getEvents() != null) {
        // entity.setEvents(request.getEvents());
        // }
    }
}
