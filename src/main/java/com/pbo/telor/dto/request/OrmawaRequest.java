package com.pbo.telor.dto.request;

import org.springframework.web.multipart.MultipartFile;

import com.pbo.telor.enums.LabType;
import com.pbo.telor.enums.OrmawaCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import com.pbo.telor.model.EventEntity;
import jakarta.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrmawaRequest {
    private String ormawaName;
    private String description;
    private String content;
    private Boolean isOpenRegistration;
    private MultipartFile icon;
    private MultipartFile background;

    /* private List<EventEntity> events; */

    private OrmawaCategory category;

    @Nullable
    private LabType labType;

    @Nullable
    private String ukmCategory;

}
