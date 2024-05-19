package com.pser.hotel.domain.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TimesaleCreateRequest {
    @NotBlank
    private long roomId;
    @NotBlank
    private int price;
    @NotBlank
    private LocalDateTime startAt;
    @NotBlank
    private LocalDateTime endAt;
}
