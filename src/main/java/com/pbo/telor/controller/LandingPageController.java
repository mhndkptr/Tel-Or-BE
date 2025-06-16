package com.pbo.telor.controller;

import com.pbo.telor.dto.response.LandingPageResponse;
import com.pbo.telor.service.LandingPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/landing")
@RequiredArgsConstructor
public class LandingPageController {

    private final LandingPageService landingPageService;

    @GetMapping
    public LandingPageResponse getLandingData() {
        return landingPageService.getLandingData();
    }
}
