package com.pbo.telor.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pbo.telor.dto.request.OrmawaRequest;
import com.pbo.telor.dto.response.OrmawaResponse;
import com.pbo.telor.enums.OrmawaCategory;
import com.pbo.telor.exception.NotFoundException;
import com.pbo.telor.mapper.OrmawaMapper;
import com.pbo.telor.model.OrmawaCommunityEntity;
import com.pbo.telor.model.OrmawaEntity;
import com.pbo.telor.model.OrmawaLaboratoryEntity;
import com.pbo.telor.model.OrmawaOrganizationEntity;
import com.pbo.telor.model.OrmawaUKMEntity;
import com.pbo.telor.repository.OrmawaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrmawaService {

    private final OrmawaRepository ormawaRepository;

    public Page<OrmawaResponse> findAllPaged(int page, int size) {
        return ormawaRepository.findAll(PageRequest.of(page, size))
                .map(OrmawaMapper::toResponse);
    }

    public OrmawaResponse getOrmawaById(UUID id) {
        OrmawaEntity entity = ormawaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ormawa not found with ID: " + id));
        return OrmawaMapper.toResponse(entity);
    }

    public OrmawaResponse createOrmawa(OrmawaRequest request) {
        OrmawaEntity entity;

        OrmawaCategory type = request.getCategory();

        switch (type) {
            case COMMUNITY -> entity = new OrmawaCommunityEntity();
            case ORGANIZATION -> entity = new OrmawaOrganizationEntity();
            case LAB -> {
                OrmawaLaboratoryEntity lab = new OrmawaLaboratoryEntity();
                lab.setLabType(request.getLabType());
                entity = lab;
            }
            case UKM -> {
                OrmawaUKMEntity ukm = new OrmawaUKMEntity();
                ukm.setUkmCategory(request.getUkmCategory());
                entity = ukm;
            }
            default -> throw new IllegalArgumentException("Unsupported ormawa category: " + type);
        }

        // Common field setter
        entity.setCategory(type);
        entity.setOrmawaName(request.getOrmawaName());
        entity.setDescription(request.getDescription());
        entity.setContent(request.getContent());
        entity.setIsOpenRegistration(request.getIsOpenRegistration());
        entity.setIcon(request.getIcon());
        entity.setBackground(request.getBackground());

        OrmawaEntity saved = ormawaRepository.save(entity);
        return OrmawaMapper.toResponse(saved);
    }

    public OrmawaResponse updateOrmawa(UUID id, OrmawaRequest request) {
        OrmawaEntity entity = ormawaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ormawa not found with ID: " + id));

        OrmawaMapper.updateEntityFromRequest(entity, request);
        OrmawaEntity updated = ormawaRepository.save(entity);
        return OrmawaMapper.toResponse(updated);
    }

    public OrmawaResponse patchOrmawa(UUID id, OrmawaRequest request) {
        OrmawaEntity entity = ormawaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ormawa not found with ID: " + id));

        if (request.getOrmawaName() != null) entity.setOrmawaName(request.getOrmawaName());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getContent() != null) entity.setContent(request.getContent());
        if (request.getIsOpenRegistration() != null) entity.setIsOpenRegistration(request.getIsOpenRegistration());
        if (request.getIcon() != null) entity.setIcon(request.getIcon());
        if (request.getBackground() != null) entity.setBackground(request.getBackground());

        // Set field khusus
        if (entity instanceof OrmawaLaboratoryEntity lab && request.getLabType() != null) {
            lab.setLabType(request.getLabType());
        }

        if (entity instanceof OrmawaUKMEntity ukm && request.getUkmCategory() != null) {
            ukm.setUkmCategory(request.getUkmCategory());
        }

        OrmawaEntity patched = ormawaRepository.save(entity);
        return OrmawaMapper.toResponse(patched);
    }

    public void deleteOrmawa(UUID id) {
        if (!ormawaRepository.existsById(id)) {
            throw new NotFoundException("Ormawa not found with ID: " + id);
        }
        ormawaRepository.deleteById(id);
    }
}
