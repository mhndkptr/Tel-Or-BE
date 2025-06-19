package com.pbo.telor.mapper;

import com.pbo.telor.dto.response.FaqResponse;
import com.pbo.telor.dto.response.LandingPageResponse;
import com.pbo.telor.model.EventBeasiswa;
import com.pbo.telor.model.EventEntity;
import com.pbo.telor.model.EventLomba;
import com.pbo.telor.model.EventSeminar;
import com.pbo.telor.model.FaqEntity;
import com.pbo.telor.model.OrmawaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LandingPageMapper {

    public List<LandingPageResponse.EventPreviewDTO> toEventPreviewDTOs(List<EventEntity> events) {
    return events.stream()
            .map(e -> {
                String eventRegion = null;
                if (e instanceof EventSeminar seminar && seminar.getEventRegion() != null) {
                    eventRegion = seminar.getEventRegion().name();
                } else if (e instanceof EventLomba lomba && lomba.getEventRegion() != null) {
                    eventRegion = lomba.getEventRegion().name();
                } else if (e instanceof EventBeasiswa beasiswa && beasiswa.getEventRegion() != null) {
                    eventRegion = beasiswa.getEventRegion().name();
                }

                return LandingPageResponse.EventPreviewDTO.builder()
                        .eventId(e.getEventId().toString())
                        .eventName(e.getEventName())
                        .image((e.getImage() != null && !e.getImage().isEmpty()) ? e.getImage().get(0) : null)
                        .eventType(e.getEventType())
                        .startEvent(e.getStartEvent() != null ? e.getStartEvent().toString() : null)
                        .description(e.getDescription())
                        .content(e.getContent())
                        .eventRegion(eventRegion)
                        .build();
            })
            .collect(Collectors.toList()); 
}

    public List<LandingPageResponse.OrmawaWithCountDTO> toOrmawaWithCountDTOs(List<OrmawaEntity> ormawas) {
        return ormawas.stream()
                .map(o -> LandingPageResponse.OrmawaWithCountDTO.builder()
                        .id(o.getId())
                        .name(o.getOrmawaName())
                        .logoUrl(o.getIcon())
                        .postCount(o.getEvents() != null ? o.getEvents().size() : 0)
                        .description(o.getDescription())
                        .content(o.getContent())
                        .category(o.getCategory() != null ? o.getCategory().name() : null)
                        .build())
                .toList();
    }

    public List<FaqResponse> toFaqResponses(List<FaqEntity> faqs) {
        return faqs.stream().map(faq -> FaqResponse.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .category(faq.getCategory())
                .build()).collect(Collectors.toList());
    }
}
