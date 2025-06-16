package com.pbo.telor.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pbo.telor.dto.response.LandingPageResponse;
import com.pbo.telor.mapper.LandingPageMapper;
import com.pbo.telor.repository.EventRepository;
import com.pbo.telor.repository.FaqRepository;
import com.pbo.telor.repository.OrmawaRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class LandingPageService {

    private final EventRepository eventRepository;
    private final OrmawaRepository ormawaRepository;
    private final FaqRepository faqRepository;
    private final LandingPageMapper landingPageMapper;

    public LandingPageResponse getLandingData() {
        var latestEvents = eventRepository.findTop3ByOrderByStartEventDesc();
        var topOrmawa = ormawaRepository.findAll()
                .stream()
                .filter(ormawa -> ormawa.getIsOpenRegistration())
                .limit(3)
                .toList();
        var latestFaqs = faqRepository.findAllByCategory(
                "umum",
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).getContent();

        return LandingPageResponse.builder()
                .latestEvents(landingPageMapper.toEventPreviewDTOs(latestEvents))
                .topOrmawa(landingPageMapper.toOrmawaWithCountDTOs(topOrmawa))
                .latestFaqs(landingPageMapper.toFaqResponses(latestFaqs))
                .build();
    }
}
