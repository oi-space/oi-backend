package com.pser.hotel.domain.hotel.dto.reservation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDeleteResponseDto {
    private String roomName;
    private LocalDateTime deletedAt;
}
