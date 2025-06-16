package com.pbo.telor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import com.pbo.telor.enums.EventType;

@Getter
@Builder
@AllArgsConstructor
public class LandingPageResponse {
    private List<EventPreviewDTO> latestEvents;
    private List<OrmawaWithCountDTO> topOrmawa;
    private List<FaqResponse> latestFaqs;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class EventPreviewDTO {
        private String eventId;
        private String eventName;
        private String image;
        private EventType eventType;
        private String startEvent;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrmawaWithCountDTO {
        private UUID id;
        private String name;
        private String logoUrl;
        private int postCount;
    }
}
