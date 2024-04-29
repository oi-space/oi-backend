package com.pser.hotel.domain.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pser.hotel.domain.model.GradeEnum;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReviewListResponseDto {

    private List<ReviewDto> reviews;
    private Long totalReviews;

    @Getter
    @Builder
    public static class ReviewDto {

        private Long userId; // 사용자 ID
        private GradeEnum grade; // 별점
        private String title; // 게시글 제목
        private String content; // 게시글 본문 내용

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        private LocalDateTime createdAt; // 등록 날짜
    }
}
