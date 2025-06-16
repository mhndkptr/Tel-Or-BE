package com.pbo.telor.dto.request;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.pbo.telor.enums.EventType;

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
    private MultipartFile image;
    private String description;
    private String content;
    private EventType eventType;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startEvent;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endEvent;
}
