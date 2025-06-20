package com.pbo.telor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import com.pbo.telor.dto.request.OrmawaRequest;
import com.pbo.telor.dto.response.OrmawaResponse;
import com.pbo.telor.enums.LabType;
import com.pbo.telor.enums.OrmawaCategory;
import com.pbo.telor.exception.NotFoundException;
import com.pbo.telor.mapper.OrmawaMapper;
import com.pbo.telor.model.OrmawaCommunityEntity;
import com.pbo.telor.model.OrmawaEntity;
import com.pbo.telor.model.UserEntity;
import com.pbo.telor.repository.OrmawaRepository;
import com.pbo.telor.repository.UserRepository;

class OrmawaServiceTest {

    @InjectMocks
    private OrmawaService ormawaService;

    @Mock
    private OrmawaRepository ormawaRepository;

    @Mock
    private UploadService uploadService;

    @Mock
    private OrmawaMapper ormawaMapper;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private OrmawaEntity createSampleOrmawa() {
        OrmawaEntity entity = mock(OrmawaEntity.class);
        UUID id = UUID.randomUUID();
        when(entity.getId()).thenReturn(id);
        when(entity.getOrmawaName()).thenReturn("Sample Ormawa");
        when(entity.getDescription()).thenReturn("Sample Description");
        when(entity.getContent()).thenReturn("Sample Content");
        when(entity.getIsOpenRegistration()).thenReturn(true);
        when(entity.getIcon()).thenReturn("icon.png");
        when(entity.getBackground()).thenReturn("bg.png");
        when(entity.getCategory()).thenReturn(OrmawaCategory.COMMUNITY);
        return entity;
    }

    @Test
    void findAll_shouldReturnListOfResponses() {
        OrmawaEntity entity = createSampleOrmawa();
        when(ormawaRepository.findAll()).thenReturn(List.of(entity));
        OrmawaResponse response = mock(OrmawaResponse.class);

        try (var mocked = mockStatic(OrmawaMapper.class)) {
            mocked.when(() -> OrmawaMapper.toResponse(entity)).thenReturn(response);

            List<OrmawaResponse> result = ormawaService.findAll();

            assertEquals(1, result.size());
            verify(ormawaRepository).findAll();
        }
    }

    @Test
    void findAllPaged_shouldReturnPagedResponses() {
        OrmawaEntity entity = createSampleOrmawa();
        Page<OrmawaEntity> page = new PageImpl<>(List.of(entity));
        when(ormawaRepository.findAll(any(PageRequest.class))).thenReturn(page);
        OrmawaResponse response = mock(OrmawaResponse.class);

        try (var mocked = mockStatic(OrmawaMapper.class)) {
            mocked.when(() -> OrmawaMapper.toResponse(entity)).thenReturn(response);

            Page<OrmawaResponse> result = ormawaService.findAllPaged(0, 10);

            assertEquals(1, result.getTotalElements());
            verify(ormawaRepository).findAll(any(PageRequest.class));
        }
    }

    @Test
    void getOrmawaById_shouldReturnResponse_whenFound() {
        OrmawaEntity entity = createSampleOrmawa();
        UUID id = entity.getId();
        when(ormawaRepository.findById(id)).thenReturn(Optional.of(entity));
        OrmawaResponse response = mock(OrmawaResponse.class);

        try (var mocked = mockStatic(OrmawaMapper.class)) {
            mocked.when(() -> OrmawaMapper.toResponse(entity)).thenReturn(response);

            OrmawaResponse result = ormawaService.getOrmawaById(id);

            assertNotNull(result);
            verify(ormawaRepository).findById(id);
        }
    }

    @Test
    void getOrmawaById_shouldThrowNotFound_whenNotFound() {
        UUID id = UUID.randomUUID();
        when(ormawaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ormawaService.getOrmawaById(id));
        verify(ormawaRepository).findById(id);
    }

    @Test
    void createOrmawa_shouldSaveAndReturnResponse_forCommunity() {
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(request.getCategory()).thenReturn(OrmawaCategory.COMMUNITY);
        when(request.getOrmawaName()).thenReturn("Central Computer Improvement");
        when(request.getDescription()).thenReturn("desc");
        when(request.getContent()).thenReturn("content");
        when(request.getIsOpenRegistration()).thenReturn(true);
        MultipartFile iconFile = mock(MultipartFile.class);
        MultipartFile bgFile = mock(MultipartFile.class);
        when(request.getIcon()).thenReturn(iconFile);
        when(request.getBackground()).thenReturn(bgFile);

        UUID userId = UUID.randomUUID();
        when(request.getUserId()).thenReturn(userId);
        UserEntity user = mock(UserEntity.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(uploadService.saveFile(anyString(), any(MultipartFile.class))).thenReturn("some-url");
        OrmawaEntity savedEntity = createSampleOrmawa();
        when(ormawaMapper.fillEntityFromRequest(any(), anyString(), anyString()))
                .thenReturn(new OrmawaCommunityEntity());
        when(ormawaRepository.save(any(OrmawaEntity.class))).thenReturn(savedEntity);
        OrmawaResponse response = mock(OrmawaResponse.class);

        try (var mocked = mockStatic(OrmawaMapper.class)) {
            mocked.when(() -> OrmawaMapper.toResponse(savedEntity)).thenReturn(response);

            OrmawaResponse result = ormawaService.createOrmawa(request);

            assertNotNull(result);
            verify(ormawaRepository).save(any(OrmawaEntity.class));
        }
    }

    @Test
    void createOrmawa_shouldSaveAndReturnResponse_forLab() {
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(request.getCategory()).thenReturn(OrmawaCategory.LAB);
        when(request.getLabType()).thenReturn(LabType.PRAKTIKUM);
        when(request.getOrmawaName()).thenReturn("Informatics Lab");
        when(request.getDescription()).thenReturn("desc");
        when(request.getContent()).thenReturn("content");
        when(request.getIsOpenRegistration()).thenReturn(true);

        MultipartFile iconFile = mock(MultipartFile.class);
        MultipartFile bgFile = mock(MultipartFile.class);
        when(request.getIcon()).thenReturn(iconFile);
        when(request.getBackground()).thenReturn(bgFile);

        UUID userId = UUID.randomUUID();
        when(request.getUserId()).thenReturn(userId);
        UserEntity user = mock(UserEntity.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(uploadService.saveFile(anyString(), any(MultipartFile.class))).thenReturn("some-url");
        OrmawaEntity savedEntity = createSampleOrmawa();
        when(ormawaMapper.fillEntityFromRequest(any(), anyString(), anyString()))
                .thenReturn(new OrmawaCommunityEntity());
        when(ormawaRepository.save(any(OrmawaEntity.class))).thenReturn(savedEntity);
        OrmawaResponse response = mock(OrmawaResponse.class);

        try (var mocked = mockStatic(OrmawaMapper.class)) {
            mocked.when(() -> OrmawaMapper.toResponse(savedEntity)).thenReturn(response);

            OrmawaResponse result = ormawaService.createOrmawa(request);

            assertNotNull(result);
            verify(ormawaRepository).save(any(OrmawaEntity.class));
        }
    }

    @Test
    void updateOrmawa_shouldUpdateAndReturnResponse_whenFound() {
        OrmawaEntity entity = createSampleOrmawa();
        UUID id = entity.getId();
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(ormawaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ormawaRepository.save(entity)).thenReturn(entity);
        OrmawaResponse response = mock(OrmawaResponse.class);

        try (var mocked = mockStatic(OrmawaMapper.class)) {
            mocked.when(() -> OrmawaMapper.updateEntityFromRequest(entity, request, "icon.png", "background.png"))
                    .thenCallRealMethod();
            mocked.when(() -> OrmawaMapper.toResponse(entity)).thenReturn(response);

            OrmawaResponse result = ormawaService.updateOrmawa(id, request);

            assertNotNull(result);
            verify(ormawaRepository).save(entity);
        }
    }

    @Test
    void updateOrmawa_shouldThrowNotFound_whenNotFound() {
        UUID id = UUID.randomUUID();
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(ormawaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ormawaService.updateOrmawa(id, request));
        verify(ormawaRepository, never()).save(any());
    }

    @Test
    void patchOrmawa_shouldPatchAndReturnResponse_whenFound() {
        OrmawaEntity entity = createSampleOrmawa();
        UUID id = entity.getId();
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(ormawaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(ormawaRepository.save(entity)).thenReturn(entity);
        OrmawaResponse response = mock(OrmawaResponse.class);

        try (var mocked = mockStatic(OrmawaMapper.class)) {
            mocked.when(() -> OrmawaMapper.toResponse(entity)).thenReturn(response);

            OrmawaResponse result = ormawaService.patchOrmawa(id, request);

            assertNotNull(result);
            verify(ormawaRepository).save(entity);
        }
    }

    @Test
    void patchOrmawa_shouldThrowNotFound_whenNotFound() {
        UUID id = UUID.randomUUID();
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(ormawaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ormawaService.patchOrmawa(id, request));
        verify(ormawaRepository, never()).save(any());
    }

    @Test
    void deleteOrmawa_shouldDelete_whenExists() {
        UUID id = UUID.randomUUID();
        when(ormawaRepository.existsById(id)).thenReturn(true);

        ormawaService.deleteOrmawa(id);

        verify(ormawaRepository).deleteById(id);
    }

    @Test
    void deleteOrmawa_shouldThrowNotFound_whenNotExists() {
        UUID id = UUID.randomUUID();
        when(ormawaRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> ormawaService.deleteOrmawa(id));
        verify(ormawaRepository, never()).deleteById(id);
    }
}