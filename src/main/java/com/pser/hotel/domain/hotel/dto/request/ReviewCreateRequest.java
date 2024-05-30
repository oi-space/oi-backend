package com.pser.hotel.domain.hotel.dto.request;

import com.pser.hotel.domain.model.GradeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ReviewCreateRequest {

    @NotNull
    private GradeEnum grade;

    @NotBlank
    @Size(min = 10, max = 500)
    private String detail;

    @NotNull
    private Long reservationId;

    private List<String> imageUrls; // 사진 등록 기능을 위한 필드 추가
}
