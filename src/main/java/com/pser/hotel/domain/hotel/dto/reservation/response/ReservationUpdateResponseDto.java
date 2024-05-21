package com.pser.hotel.domain.hotel.dto.reservation.response;

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
public class ReservationUpdateResponseDto {
    private String roomName;
    // 시작일
    private LocalDate startAt;
    // 종료일
    private LocalDate endAt;
    // 성인 가용인원
    private Integer adultCapacity;
    // 어린이 가용인원
    private Integer childCapacity;
    // 예약 상태
    private ReservationEnum status;

}
