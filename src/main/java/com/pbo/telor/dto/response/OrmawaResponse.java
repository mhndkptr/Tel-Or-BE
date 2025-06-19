package com.pbo.telor.dto.response;

import java.util.UUID;

import com.pbo.telor.enums.LabType;
import com.pbo.telor.enums.OrmawaCategory;

import jakarta.persistence.Column;
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
public class OrmawaResponse {
    private UUID id;
    @Column(name = "name")
    private String ormawaName;
    private String description;
    private String content;
    private Boolean isOpenRegistration;
    private String icon;
    private String background;

    private OrmawaCategory category; // COMMUNITY, LABORATORY, UKM, ORGANIZATION

    private LabType labType; //Praktikum, Research
    private String ukmCategory;
}
