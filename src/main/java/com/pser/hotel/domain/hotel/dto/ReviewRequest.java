package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.model.GradeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRequest {
    @NotNull
    private GradeEnum grade; // 별점

    @NotNull
    @Size(min = 10, max = 500)
    private String detail; // 상세 내용

    @NotNull
    private Long reservationId; // 예약 ID
}