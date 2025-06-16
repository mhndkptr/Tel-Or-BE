package com.pbo.telor.service;

import com.pbo.telor.dto.request.FaqRequest;
import com.pbo.telor.dto.response.FaqResponse;
import com.pbo.telor.exception.NotFoundException;
import com.pbo.telor.mapper.FaqMapper;
import com.pbo.telor.model.FaqEntity;
import com.pbo.telor.repository.FaqRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FaqServiceTest {

    @Mock
    private FaqRepository faqRepository;
    @Mock
    private FaqMapper faqMapper;
    @InjectMocks
    private FaqService faqService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnFaqResponse_whenGetByIdAndDataExists() {
        UUID id = UUID.randomUUID();
        FaqEntity entity = FaqEntity.builder().id(id).question("Q").answer("A").category("C").build();
        FaqResponse response = FaqResponse.builder().id(id).question("Q").answer("A").category("C").build();

        when(faqRepository.findById(id)).thenReturn(Optional.of(entity));
        when(faqMapper.toResponse(entity)).thenReturn(response);

        FaqResponse result = faqService.getFaqById(id);

        assertEquals(response, result);
        verify(faqRepository).findById(id);
    }

    @Test
    void shouldThrowNotFoundException_whenGetByIdAndDataNotExists() {
        UUID id = UUID.randomUUID();
        when(faqRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> faqService.getFaqById(id));
    }

    @Test
    void shouldSaveFaqAndReturnResponse_whenCreateFaqCalled() {
        FaqRequest request = FaqRequest.builder().question("Q").answer("A").category("C").build();
        FaqEntity entity = FaqEntity.builder().question("Q").answer("A").category("C").build();
        FaqEntity savedEntity = FaqEntity.builder().id(UUID.randomUUID()).question("Q").answer("A").category("C").build();
        FaqResponse response = FaqResponse.builder().id(savedEntity.getId()).question("Q").answer("A").category("C").build();

        when(faqMapper.toEntity(request)).thenReturn(entity);
        when(faqRepository.save(entity)).thenReturn(savedEntity);
        when(faqMapper.toResponse(savedEntity)).thenReturn(response);

        FaqResponse result = faqService.createFaq(request);

        assertEquals(response, result);
        verify(faqRepository).save(entity);
    }

    @Test
    void shouldDeleteFaq_whenDataExists() {
        UUID id = UUID.randomUUID();
        when(faqRepository.existsById(id)).thenReturn(true);

        faqService.deleteFaq(id);

        verify(faqRepository).deleteById(id);
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteAndDataNotExists() {
        UUID id = UUID.randomUUID();
        when(faqRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> faqService.deleteFaq(id));
    }
}
