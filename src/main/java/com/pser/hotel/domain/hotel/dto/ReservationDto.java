package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;

    private Long userId;

    private Long roomId;

    private Integer price;

    private LocalDate startAt;

    private LocalDate endAt;

    private LocalTime checkIn;

    private LocalTime checkOut;

    private Integer visitorCount;

    private Integer adultCount;

    private Integer childCount;

    private ReservationStatusEnum status;
}
