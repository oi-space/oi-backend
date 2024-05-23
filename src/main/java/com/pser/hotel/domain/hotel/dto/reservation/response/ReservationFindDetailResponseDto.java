package com.pser.hotel.domain.hotel.dto.reservation.response;

import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationFindDetailResponseDto {

    // 예약 번호 차후 수정, 삭제 등에 사용
    private Long id;
    // 예약자 이메일
    private String userEmail;
    // 예약 방 이름
    private String roomName;
    // 가격
    private Integer price;
    // 시작일
    private LocalDate startAt;
    // 종료일
    private LocalDate endAt;
    // 가용인원
    private Integer reservationCapacity;
    // 성인 가용인원
    private Integer adultCapacity;
    // 어린이 가용인원
    private Integer childCapacity;
    // 예약 상태
    private ReservationStatusEnum status;

    public ReservationFindDetailResponseDto(Reservation reservation, String userEmail, String roomName) {
        this.id = reservation.getId();
        this.userEmail = userEmail;
        this.roomName = roomName;
        this.price = reservation.getPrice();
        this.startAt = reservation.getStartAt();
        this.endAt = reservation.getEndAt();
        this.reservationCapacity = reservation.getVisitorCount();
        this.adultCapacity = reservation.getAdultCount();
        this.childCapacity = reservation.getChildCount();
        this.status = reservation.getStatus();

    }
}
