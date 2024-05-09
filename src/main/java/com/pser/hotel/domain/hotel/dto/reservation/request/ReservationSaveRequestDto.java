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
public class ReservationSaveRequestDto {
    // 회원 탐색을 위한 user email
    private String userEmail;
    // 객실 예약을 위한 room name
    private String roomName;
    private Integer price;
    private LocalDate startAt;
    private LocalDate endAt;
    private Integer reservationCapacity;
    private Integer adultCapacity;
    private Integer childCapacity;
    private ReservationEnum status;
}
