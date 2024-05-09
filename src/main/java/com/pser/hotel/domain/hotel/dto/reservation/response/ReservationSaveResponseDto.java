package com.pser.hotel.domain.hotel.dto.reservation.response;

import com.pser.hotel.domain.hotel.domain.Reservation;
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
public class ReservationSaveResponseDto {
    private String userEmail;
    private String roomName;
    private Integer price;
    private LocalDate startAt;
    private LocalDate endAt;
    private Integer reservationCapacity;
    private Integer adultCapacity;
    private Integer childCapacity;
    private ReservationEnum status;


    public ReservationSaveResponseDto(Reservation reservation, String userEmail, String roomName){
        this.userEmail = userEmail;
        this.roomName = roomName;
        this.price = reservation.getPrice();
        this.startAt = reservation.getStartAt();
        this.endAt = reservation.getEndAt();
        this.reservationCapacity = reservation.getReservationCapacity();
        this.adultCapacity = reservation.getAdultCapacity();
        this.childCapacity = reservation.getChildCapacity();
        this.status =reservation.getStatus();

    }
}
