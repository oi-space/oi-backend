package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.model.GradeEnum;
import com.pser.hotel.global.common.request.SearchQuery;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewSearchRequest extends SearchQuery {

    private GradeEnum grade;
    private String detail;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double rating; // rating 필드 추가
    private String content; // content 필드 추가

    @Builder
    public ReviewSearchRequest(String keyword, LocalDateTime createdAfter, LocalDateTime createdBefore,
                               LocalDateTime updatedAfter, LocalDateTime updatedBefore, GradeEnum grade, String detail,
                               LocalDateTime startDate, LocalDateTime endDate, double rating, String content) {
        super(keyword, createdAfter, createdBefore, updatedAfter, updatedBefore);
        this.grade = grade;
        this.detail = detail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating; // rating 초기화
        this.content = content; // content 초기화
    }

    // getRating 메서드 추가
    public int getRating() {
        return (int) rating;
    }

    // getContent 메서드 추가
    public String getContent() {
        return content;
    }

    // getCreatedAt 메서드 추가
    public LocalDateTime getCreatedAt() {
        return super.getCreatedAfter(); // createdAfter 필드를 반환
    }

    // getUpdatedAt 메서드 추가
    public LocalDateTime getUpdatedAt() {
        return super.getUpdatedAfter(); // updatedAfter 필드를 반환
    }
}
