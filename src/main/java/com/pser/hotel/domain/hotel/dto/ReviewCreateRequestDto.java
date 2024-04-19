package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.model.GradeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ReviewCreateRequestDto {

    @NotNull // 리뷰 등급은 필수 입력 값임
    private GradeEnum grade;

    @NotBlank // 상세 내용은 비어 있으면 안 됨
    @Length(min = 10, max = 500) // 상세 내용의 길이 제한
    private String detail;

    @NotNull // 연결된 예약 ID는 필수 입력 값임
    private Long reservationId;
}
