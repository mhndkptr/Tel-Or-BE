package com.pbo.telor.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String name;
    private String message;
    private List<ValidationDetail> validation;
}

