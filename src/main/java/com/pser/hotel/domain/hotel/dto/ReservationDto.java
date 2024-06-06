package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long userId;

    private String merchantUid;

    private String impUid;

    private RoomDto room;

    private HotelDto hotel;

    private Integer price;

    private LocalDate startAt;

    private LocalDate endAt;

    private Integer visitorCount;

    private Integer adultCount;

    private Integer childCount;

    private ReservationStatusEnum status;
}
