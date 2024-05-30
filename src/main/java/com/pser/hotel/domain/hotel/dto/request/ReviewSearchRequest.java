package com.pser.hotel.domain.hotel.dto.request;

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
    private Double rating; // double에서 Double로 변경
    private String content;

    @Builder
    public ReviewSearchRequest(String keyword, LocalDateTime createdAfter, LocalDateTime createdBefore,
                               LocalDateTime updatedAfter, LocalDateTime updatedBefore, GradeEnum grade, String detail,
                               LocalDateTime startDate, LocalDateTime endDate, Double rating, String content) {
        super(keyword, createdAfter, createdBefore, updatedAfter, updatedBefore);
        this.grade = grade;
        this.detail = detail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
        this.content = content;
    }

    public Integer getRating() {
        return rating != null ? rating.intValue() : null; // null 체크 추가
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return super.getCreatedAfter();
    }

    public LocalDateTime getUpdatedAt() {
        return super.getUpdatedAfter();
    }
}
