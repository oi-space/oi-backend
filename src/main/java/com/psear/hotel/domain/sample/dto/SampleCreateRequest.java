package com.psear.hotel.domain.sample.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SampleCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;
}