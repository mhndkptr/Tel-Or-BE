package com.pbo.telor.dto.request;
import com.pbo.telor.enums.EventType;

import java.util.Date;
import java.util.List;

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
public class EventRequest {
    private String eventName;
    private List<String> image;
    private String description;
    private String content;
    private EventType eventType;
    private Date startEvent;
    private Date endEvent;
}
