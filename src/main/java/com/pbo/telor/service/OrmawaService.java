package com.pbo.telor.service;

import java.util.List;
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
import com.pbo.telor.model.UserEntity;
import com.pbo.telor.repository.OrmawaRepository;
import com.pbo.telor.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrmawaService {

    private final UserRepository userRepository;
    private final OrmawaRepository ormawaRepository;
    private final UploadService uploadService;
    private final OrmawaMapper ormawaMapper;

    public List<OrmawaResponse> findAll() {
        return ormawaRepository.findAll()
                .stream()
                .map(OrmawaMapper::toResponse)
                .toList();
    }

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
        String iconUrl = uploadService.saveFile("ormawa/icon", request.getIcon());
        String bgUrl = uploadService.saveFile("ormawa/background", request.getBackground());

        OrmawaEntity entity = ormawaMapper.fillEntityFromRequest(request, iconUrl, bgUrl);
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + request.getUserId()));

        OrmawaEntity savedOrmawa = ormawaRepository.save(entity);

        user.setOrmawa(savedOrmawa);
        savedOrmawa.setUser(user);

        userRepository.save(user);

        return ormawaMapper.toResponse(savedOrmawa);
    }

    public OrmawaResponse updateOrmawa(UUID id, OrmawaRequest request) {
        OrmawaEntity entity = ormawaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ormawa not found with ID: " + id));

        String iconUrl = (request.getIcon() != null && !request.getIcon().isEmpty())
                ? uploadService.saveFile("ormawa/icon", request.getIcon())
                : null;

        String bgUrl = (request.getBackground() != null && !request.getBackground().isEmpty())
                ? uploadService.saveFile("ormawa/background", request.getBackground())
                : null;

        ormawaMapper.updateEntityFromRequest(entity, request, iconUrl, bgUrl);
        OrmawaEntity updated = ormawaRepository.save(entity);
        return ormawaMapper.toResponse(updated);
    }

    public OrmawaResponse patchOrmawa(UUID id, OrmawaRequest request) {
        OrmawaEntity entity = ormawaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ormawa not found with ID: " + id));

        if (request.getOrmawaName() != null)
            entity.setOrmawaName(request.getOrmawaName());
        if (request.getDescription() != null)
            entity.setDescription(request.getDescription());
        if (request.getContent() != null)
            entity.setContent(request.getContent());
        if (request.getIsOpenRegistration() != null)
            entity.setIsOpenRegistration(request.getIsOpenRegistration());
        if (request.getIcon() != null && !request.getIcon().isEmpty()) {
            entity.setIcon(uploadService.saveFile("ormawa/icon", request.getIcon()));
        }

        if (request.getBackground() != null && !request.getBackground().isEmpty()) {
            entity.setBackground(uploadService.saveFile("ormawa/background", request.getBackground()));
        }
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
