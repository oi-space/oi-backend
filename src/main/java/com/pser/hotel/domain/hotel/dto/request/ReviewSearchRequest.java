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
    private double rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long reservationId;


    @Builder
    public ReviewSearchRequest(String keyword, LocalDateTime createdAfter, LocalDateTime createdBefore,
                               LocalDateTime updatedAfter, LocalDateTime updatedBefore, GradeEnum grade, String detail,
                               LocalDateTime startDate, LocalDateTime endDate, double rating, String content,
                               LocalDateTime createdAt, LocalDateTime updatedAt, Long reservationId) {
        super(keyword, createdAfter, createdBefore, updatedAfter, updatedBefore);
        this.grade = grade;
        this.detail = detail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.reservationId = reservationId;
    }

}
