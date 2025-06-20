package com.pbo.telor.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pbo.telor.enums.LabType;
import com.pbo.telor.enums.OrmawaCategory;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrmawaResponse {
    private UUID id;
    @Column(name = "name")
    private String ormawaName;
    private String description;
    private String content;
    private Boolean isOpenRegistration;
    private String icon;
    private String background;

    private List<EventResponse> events;
    private UserResponse user;

    private OrmawaCategory category; // COMMUNITY, LABORATORY, UKM, ORGANIZATION

    private LabType labType; // Praktikum, Research
    private String ukmCategory;
}
