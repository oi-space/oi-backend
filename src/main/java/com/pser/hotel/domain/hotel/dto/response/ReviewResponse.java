package com.pser.hotel.domain.hotel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pser.hotel.domain.model.GradeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class ReviewResponse {
    Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    LocalDateTime updatedAt;

    GradeEnum grade;

    String detail;

    List<String> imageUrls; // 리뷰와 관련된 이미지 URL 목록
}
