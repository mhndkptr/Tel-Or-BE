package com.pbo.telor.mapper;

import com.pbo.telor.dto.response.FaqResponse;
import com.pbo.telor.dto.response.LandingPageResponse;
import com.pbo.telor.model.EventEntity;
import com.pbo.telor.model.FaqEntity;
import com.pbo.telor.model.OrmawaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LandingPageMapper {

    public List<LandingPageResponse.EventPreviewDTO> toEventPreviewDTOs(List<EventEntity> events) {
        return events.stream().map(e ->
                LandingPageResponse.EventPreviewDTO.builder()
                        .eventId(e.getEventId().toString()) // convert UUID to String
                        .eventName(e.getEventName())
                        .image(e.getImage() != null && !e.getImage().isEmpty() ? e.getImage().get(0) : null)
                        .eventType(e.getEventType())
                        .startEvent(e.getStartEvent().toString())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<LandingPageResponse.OrmawaWithCountDTO> toOrmawaWithCountDTOs(List<OrmawaEntity> ormawas) {
        return ormawas.stream().map(o ->
                LandingPageResponse.OrmawaWithCountDTO.builder()
                        .id(o.getId())
                        .name(o.getOrmawaName()) // use correct getter
                        .logoUrl(o.getIcon())
                        .postCount(0)
                        .build()
        ).collect(Collectors.toList());
    }

    public List<FaqResponse> toFaqResponses(List<FaqEntity> faqs) {
        return faqs.stream().map(faq ->
                FaqResponse.builder()
                        .id(faq.getId())
                        .question(faq.getQuestion())
                        .answer(faq.getAnswer())
                        .build()
        ).collect(Collectors.toList());
    }
}
