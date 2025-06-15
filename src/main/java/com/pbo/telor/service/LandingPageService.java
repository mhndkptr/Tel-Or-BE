package com.pbo.telor.service;

import com.pbo.telor.dto.response.LandingPageResponse;
import com.pbo.telor.mapper.LandingPageMapper;
import com.pbo.telor.repository.EventRepository;
import com.pbo.telor.repository.FaqRepository;
import com.pbo.telor.repository.OrmawaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LandingPageService {

    private final EventRepository eventRepository;
    private final OrmawaRepository ormawaRepository;
    private final FaqRepository faqRepository;
    private final LandingPageMapper landingPageMapper;

    public LandingPageResponse getLandingData() {
        var latestEvents = eventRepository.findTop3ByOrderByStartEventDesc();
        var topOrmawa = ormawaRepository.findTop3OrmawaByPostCount();
        var latestFaqs = faqRepository.findTop3ByOrderByCreatedAtDesc();

        return LandingPageResponse.builder()
                .latestEvents(landingPageMapper.toEventPreviewDTOs(latestEvents))
                .topOrmawa(landingPageMapper.toOrmawaWithCountDTOs(topOrmawa))
                .latestFaqs(landingPageMapper.toFaqResponses(latestFaqs))
                .build();
    }
}