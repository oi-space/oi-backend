package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long userId;

    private Long roomId;

    private Integer price;

    private LocalDate startAt;

    private LocalDate endAt;

    private Integer visitorCount;

    private Integer adultCount;

    private Integer childCount;

    private ReservationStatusEnum status;
}
