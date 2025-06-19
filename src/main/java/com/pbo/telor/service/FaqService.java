package com.pbo.telor.service;

import com.pbo.telor.dto.request.FaqRequest;
import com.pbo.telor.dto.response.FaqResponse;
import com.pbo.telor.exception.NotFoundException;
import com.pbo.telor.mapper.FaqMapper;
import com.pbo.telor.model.FaqEntity;
import com.pbo.telor.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;
    private final FaqMapper faqMapper;

    public List<FaqResponse> getAllFaqs() {
        return faqRepository.findAll().stream()
                .map(faqMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Page<FaqResponse> findAllPaged(Integer page, Integer limit, String category, String search) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), limit);
        Page<FaqEntity> faqEntities;
        if (search != null && !search.isBlank()) {
            faqEntities = faqRepository.findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCase(search, search,
                    pageable);
        } else if (category != null && !category.isBlank()) {
            faqEntities = faqRepository.findAllByCategory(category, pageable);
        } else {
            faqEntities = faqRepository.findAll(pageable);
        }
        return faqEntities.map(faqMapper::toResponse);
    }

    public FaqResponse getFaqById(UUID id) {
        FaqEntity faq = faqRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("FAQ not found with id: " + id));
        return faqMapper.toResponse(faq);
    }

    public FaqResponse createFaq(FaqRequest request) {
        FaqEntity faq = faqMapper.toEntity(request);
        FaqEntity savedFaq = faqRepository.save(faq);
        return faqMapper.toResponse(savedFaq);
    }

    public FaqResponse updateFaq(UUID id, FaqRequest request) {
        FaqEntity faq = faqRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("FAQ not found with id: " + id));

        faq.setQuestion(request.question());
        faq.setAnswer(request.answer());
        faq.setCategory(request.category());

        FaqEntity updatedFaq = faqRepository.save(faq);
        return faqMapper.toResponse(updatedFaq);
    }

    public void deleteFaq(UUID id) {
        if (!faqRepository.existsById(id)) {
            throw new NotFoundException("FAQ not found with id: " + id);
        }
        faqRepository.deleteById(id);
    }

    public List<FaqResponse> getAllFaqsByCategory(String category) {
        return faqRepository.findAllByCategory(category, Pageable.unpaged())
                .stream()
                .map(faqMapper::toResponse)
                .collect(Collectors.toList());
    }
}