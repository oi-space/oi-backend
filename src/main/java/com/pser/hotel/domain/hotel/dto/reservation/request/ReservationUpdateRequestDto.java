package com.pser.hotel.domain.hotel.dto.reservation.request;

import com.pser.hotel.domain.hotel.domain.ReservationEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationUpdateRequestDto {
    private String roomName;
    private LocalDate startAt;
    private LocalDate endAt;
    private Integer adultCapacity;
    private Integer childCapacity;
    private ReservationEnum status;
}
