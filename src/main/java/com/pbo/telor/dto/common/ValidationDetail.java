package com.pbo.telor.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationDetail {
    private String name;
    private String message;
}
