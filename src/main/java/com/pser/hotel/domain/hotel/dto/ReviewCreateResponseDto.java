package com.pser.hotel.domain.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pser.hotel.domain.model.GradeEnum;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCreateResponseDto {

    private Long id;

    private GradeEnum grade;

    private String detail;=

    private Long reservationId; // 연결된 예약 ID

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt; // 생성 시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt; // 수정 시간
}
