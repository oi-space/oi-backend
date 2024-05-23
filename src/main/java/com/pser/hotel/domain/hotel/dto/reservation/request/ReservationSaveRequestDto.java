package com.pser.hotel.domain.hotel.dto.reservation.request;

import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String userEmail;
    // 객실 예약을 위한 room name
    @NotBlank
    private String roomName;
    @NotBlank
    @Min(0)
    private Integer price;
    @NotBlank
    private LocalDate startAt;
    @NotBlank
    private LocalDate endAt;
    @NotBlank
    @Min(0)
    private Integer reservationCapacity;
    @NotBlank
    @Min(0)
    private Integer adultCapacity;
    @NotBlank
    @Min(0)
    private Integer childCapacity;
    @NotBlank
    private ReservationStatusEnum status;
}
