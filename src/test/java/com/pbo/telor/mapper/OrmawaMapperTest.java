package com.pbo.telor.mapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.pbo.telor.dto.request.OrmawaRequest;
import com.pbo.telor.dto.response.OrmawaResponse;
import com.pbo.telor.enums.LabType;
import com.pbo.telor.enums.OrmawaCategory;
import com.pbo.telor.model.OrmawaCommunityEntity;
import com.pbo.telor.model.OrmawaEntity;
import com.pbo.telor.model.OrmawaLaboratoryEntity;
import com.pbo.telor.model.OrmawaOrganizationEntity;
import com.pbo.telor.model.OrmawaUKMEntity;

import org.springframework.web.multipart.MultipartFile;

class OrmawaMapperTest {

    @Test
    void fillEntityFromRequest_shouldMapCommunity() {
        OrmawaRequest request = mock(OrmawaRequest.class);
        MultipartFile iconFile = mock(MultipartFile.class);
        MultipartFile bgFile = mock(MultipartFile.class);
        when(request.getCategory()).thenReturn(OrmawaCategory.COMMUNITY);
        when(request.getOrmawaName()).thenReturn("Community");
        when(request.getDescription()).thenReturn("desc");
        when(request.getContent()).thenReturn("content");
        when(request.getIsOpenRegistration()).thenReturn(true);
        when(iconFile.isEmpty()).thenReturn(false);
        when(bgFile.isEmpty()).thenReturn(false);
        when(iconFile.getOriginalFilename()).thenReturn("icon.png");
        when(bgFile.getOriginalFilename()).thenReturn("bg.png");

        OrmawaMapper ormawaMapper = new OrmawaMapper(); 
        OrmawaEntity entity = ormawaMapper.fillEntityFromRequest(request, "icon.png", "bg.png");

        assertTrue(entity instanceof OrmawaCommunityEntity);
        assertEquals("Community", entity.getOrmawaName());
        assertEquals("desc", entity.getDescription());
        assertEquals("content", entity.getContent());
        assertTrue(entity.getIsOpenRegistration());
        assertTrue(entity.getIsOpenRegistration());
        assertEquals("icon.png", entity.getIcon());
        assertEquals("bg.png", entity.getBackground());
        assertEquals(OrmawaCategory.COMMUNITY, entity.getCategory());
    }

    @Test
    void fillEntityFromRequest_shouldMapLab() {
        OrmawaRequest request = mock(OrmawaRequest.class);
        MultipartFile iconFile = mock(MultipartFile.class);
        MultipartFile bgFile = mock(MultipartFile.class);
        when(request.getCategory()).thenReturn(OrmawaCategory.LAB);
        when(request.getLabType()).thenReturn(LabType.PRAKTIKUM);
        when(request.getOrmawaName()).thenReturn("Lab");
        when(request.getDescription()).thenReturn("desc");
        when(request.getContent()).thenReturn("content");
        when(request.getIsOpenRegistration()).thenReturn(false);
        when(iconFile.isEmpty()).thenReturn(false);
        when(bgFile.isEmpty()).thenReturn(false);
        when(iconFile.getOriginalFilename()).thenReturn("icon.png");
        when(bgFile.getOriginalFilename()).thenReturn("bg.png");

        OrmawaMapper ormawaMapper = new OrmawaMapper(); 
        OrmawaEntity entity = ormawaMapper.fillEntityFromRequest(request, "icon.png", "bg.png");

        assertTrue(entity instanceof OrmawaLaboratoryEntity);
        assertEquals(LabType.PRAKTIKUM, ((OrmawaLaboratoryEntity) entity).getLabType());
    }

    @Test
    void fillEntityFromRequest_shouldMapUKM() {
        OrmawaRequest request = mock(OrmawaRequest.class);
        MultipartFile iconFile = mock(MultipartFile.class);
        MultipartFile bgFile = mock(MultipartFile.class);
        when(request.getCategory()).thenReturn(OrmawaCategory.UKM);
        when(request.getUkmCategory()).thenReturn("Olahraga");
        when(request.getOrmawaName()).thenReturn("UKM");
        when(request.getDescription()).thenReturn("desc");
        when(request.getContent()).thenReturn("content");
        when(request.getIsOpenRegistration()).thenReturn(false);
        when(iconFile.isEmpty()).thenReturn(false);
        when(bgFile.isEmpty()).thenReturn(false);
        when(iconFile.getOriginalFilename()).thenReturn("icon.png");
        when(bgFile.getOriginalFilename()).thenReturn("bg.png");

        OrmawaMapper ormawaMapper = new OrmawaMapper(); 
        OrmawaEntity entity = ormawaMapper.fillEntityFromRequest(request, "icon.png", "bg.png");

        assertTrue(entity instanceof OrmawaUKMEntity);
        assertEquals("Olahraga", ((OrmawaUKMEntity) entity).getUkmCategory());
    }

    @Test
    void fillEntityFromRequest_shouldMapOrganization() {
        OrmawaRequest request = mock(OrmawaRequest.class);
        MultipartFile iconFile = mock(MultipartFile.class);
        MultipartFile bgFile = mock(MultipartFile.class);
        when(request.getCategory()).thenReturn(OrmawaCategory.ORGANIZATION);
        when(request.getOrmawaName()).thenReturn("Org");
        when(request.getDescription()).thenReturn("desc");
        when(request.getContent()).thenReturn("content");
        when(request.getIsOpenRegistration()).thenReturn(false);
        when(iconFile.isEmpty()).thenReturn(false);
        when(bgFile.isEmpty()).thenReturn(false);
        when(iconFile.getOriginalFilename()).thenReturn("icon.png");
        when(bgFile.getOriginalFilename()).thenReturn("bg.png");

        OrmawaMapper ormawaMapper = new OrmawaMapper(); 
        OrmawaEntity entity = ormawaMapper.fillEntityFromRequest(request, "icon.png", "bg.png");

        assertTrue(entity instanceof OrmawaOrganizationEntity);
    }

    @Test
    void fillEntityFromRequest_shouldThrowOnInvalidCategory() {
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(request.getCategory()).thenReturn(null);

        OrmawaMapper ormawaMapper = new OrmawaMapper();
        assertThrows(
            IllegalArgumentException.class,
            () -> ormawaMapper.fillEntityFromRequest(request, "", "")
        );
    }

    @Test
    void toResponse_shouldMapCommonFields() {
        OrmawaCommunityEntity entity = new OrmawaCommunityEntity();
        UUID id = UUID.randomUUID();
        entity.setId(id);
        entity.setOrmawaName("Community");
        entity.setDescription("desc");
        entity.setContent("content");
        entity.setIsOpenRegistration(true);
        entity.setIcon("icon.png");
        entity.setBackground("bg.png");
        entity.setCategory(OrmawaCategory.COMMUNITY);
        entity.setUser(mock(com.pbo.telor.model.UserEntity.class));

        OrmawaResponse response = OrmawaMapper.toResponse(entity);

        assertEquals(id, response.getId());
        assertEquals("Community", response.getOrmawaName());
        assertEquals("desc", response.getDescription());
        assertEquals("content", response.getContent());
        assertTrue(response.getIsOpenRegistration());
        assertEquals("icon.png", response.getIcon());
        assertEquals("bg.png", response.getBackground());
        assertEquals(OrmawaCategory.COMMUNITY, response.getCategory());
    }

    @Test
    void toResponse_shouldMapLabType() {
        OrmawaLaboratoryEntity entity = new OrmawaLaboratoryEntity();
        entity.setLabType(LabType.RESEARCH);
        entity.setId(UUID.randomUUID());
        entity.setOrmawaName("Lab");
        entity.setCategory(OrmawaCategory.LAB);
        entity.setUser(mock(com.pbo.telor.model.UserEntity.class)); 

        OrmawaResponse response = OrmawaMapper.toResponse(entity);

        assertEquals(LabType.RESEARCH, response.getLabType());
    }

    @Test
    void toResponse_shouldMapUkmCategory() {
        OrmawaUKMEntity entity = new OrmawaUKMEntity();
        entity.setUkmCategory("Olahraga");
        entity.setId(UUID.randomUUID());
        entity.setOrmawaName("UKM");
        entity.setCategory(OrmawaCategory.UKM);
        entity.setUser(mock(com.pbo.telor.model.UserEntity.class)); 

        OrmawaResponse response = OrmawaMapper.toResponse(entity);

        assertEquals("Olahraga", response.getUkmCategory());
    }

    @Test
    void updateEntityFromRequest_shouldUpdateCommonFields() {
        OrmawaCommunityEntity entity = new OrmawaCommunityEntity();
        OrmawaRequest request = mock(OrmawaRequest.class);
        MultipartFile iconFile = mock(MultipartFile.class);
        MultipartFile bgFile = mock(MultipartFile.class);
        when(request.getOrmawaName()).thenReturn("Updated");
        when(request.getDescription()).thenReturn("desc");
        when(request.getContent()).thenReturn("content");
        when(request.getIsOpenRegistration()).thenReturn(false);
        when(iconFile.isEmpty()).thenReturn(false);
        when(bgFile.isEmpty()).thenReturn(false);
        when(iconFile.getOriginalFilename()).thenReturn("icon.png");
        when(bgFile.getOriginalFilename()).thenReturn("bg.png");

        OrmawaMapper.updateEntityFromRequest(entity, request, "icon.png", "bg.png");

        assertEquals("Updated", entity.getOrmawaName());
        assertEquals("desc", entity.getDescription());
        assertEquals("content", entity.getContent());
        assertFalse(entity.getIsOpenRegistration());
        assertEquals("icon.png", entity.getIcon());
        assertEquals("bg.png", entity.getBackground());
    }

    @Test
    void updateEntityFromRequest_shouldUpdateLabTypeIfPresent() {
        OrmawaLaboratoryEntity entity = new OrmawaLaboratoryEntity();
        entity.setLabType(LabType.PRAKTIKUM);
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(request.getLabType()).thenReturn(LabType.PRAKTIKUM);
        when(request.getOrmawaName()).thenReturn("Lab");

        OrmawaMapper.updateEntityFromRequest(entity, request, "icon.png", "bg.png");

        assertEquals(LabType.PRAKTIKUM, entity.getLabType());
    }

    @Test
    void updateEntityFromRequest_shouldNotUpdateLabTypeIfNull() {
        OrmawaLaboratoryEntity entity = new OrmawaLaboratoryEntity();
        entity.setLabType(LabType.RESEARCH);
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(request.getLabType()).thenReturn(null);
        when(request.getOrmawaName()).thenReturn("Lab");

        OrmawaMapper.updateEntityFromRequest(entity, request, "icon.png", "bg.png");

        assertEquals(LabType.RESEARCH, entity.getLabType());
    }

    @Test
    void updateEntityFromRequest_shouldUpdateUkmCategoryIfPresent() {
        OrmawaUKMEntity entity = new OrmawaUKMEntity();
        entity.setUkmCategory("Old");
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(request.getUkmCategory()).thenReturn("NewUkm");
        when(request.getOrmawaName()).thenReturn("UKM");

        OrmawaMapper.updateEntityFromRequest(entity, request, "icon.png", "bg.png");

        assertEquals("NewUkm", entity.getUkmCategory());
    }

    @Test
    void updateEntityFromRequest_shouldNotUpdateUkmCategoryIfNull() {
        OrmawaUKMEntity entity = new OrmawaUKMEntity();
        entity.setUkmCategory("Old");
        OrmawaRequest request = mock(OrmawaRequest.class);
        when(request.getUkmCategory()).thenReturn(null);
        when(request.getOrmawaName()).thenReturn("UKM");

        OrmawaMapper.updateEntityFromRequest(entity, request, "icon.png", "bg.png");

        assertEquals("Old", entity.getUkmCategory());
    }
}