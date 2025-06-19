package com.pbo.telor.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FaqRequest(
    @NotBlank(message = "Question is required")
    String question,

    @NotBlank(message = "Answer is required")
    String answer,

    @NotBlank(message = "Category is required")
    String category
) {}