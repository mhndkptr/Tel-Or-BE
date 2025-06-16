package com.pbo.telor.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pbo.telor.dto.response.EventResponse;
import com.pbo.telor.dto.response.FaqResponse;
import com.pbo.telor.dto.response.LandingPageResponse;
import com.pbo.telor.dto.response.OrmawaResponse;
import com.pbo.telor.service.EventService;
import com.pbo.telor.service.FaqService;
import com.pbo.telor.service.LandingPageService;
import com.pbo.telor.service.OrmawaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Landing Page")
@RestController
@RequestMapping("/api/v1/landing")
@RequiredArgsConstructor
public class LandingPageController {

    private final LandingPageService landingPageService;
    private final OrmawaService ormawaService;
    private final FaqService faqService;
    private final EventService eventService;

    @GetMapping
    public LandingPageResponse getLandingData() {
        return landingPageService.getLandingData();
    }

    @GetMapping("/ormawa")
    public List<OrmawaResponse> getAllOrmawa() {
        return ormawaService.findAll();
    }

    @GetMapping("/faq")
    public List<FaqResponse> getAllFaq() {
        return faqService.getAllFaqs();
    }

    @GetMapping("/event")
    public List<EventResponse> getAllEvent() {
        return eventService.findAll();
    }
}