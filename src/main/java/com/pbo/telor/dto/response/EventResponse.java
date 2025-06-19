package com.pbo.telor.dto.response;

import java.util.List;
import java.util.UUID;

import com.pbo.telor.enums.EventType;
import com.pbo.telor.enums.EventRegion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private UUID eventId;
    private String eventName;
    private List<String> image;
    private String description;
    private String prize;
    private String content;
    private EventType eventType;
    private EventRegion eventRegion;
    private String startEvent;
    private String endEvent;
    private UUID ormawaId;
}
