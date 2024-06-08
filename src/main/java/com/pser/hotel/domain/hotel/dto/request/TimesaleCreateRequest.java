package com.pser.hotel.domain.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
