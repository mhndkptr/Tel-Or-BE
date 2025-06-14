package com.pbo.telor.dto.request;

import com.pbo.telor.enums.LabType;
import com.pbo.telor.enums.OrmawaCategory;

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
public class OrmawaRequest {
    private String ormawaName;
    private String description;
    private String content;
    private Boolean isOpenRegistration;
    private String icon;
    private String background;

    private OrmawaCategory category;

    private LabType labType;         
    private String ukmCategory;      
}
