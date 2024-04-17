package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Review extends BaseEntity {
    private String grade;

    @Length(min = 10, max = 500) // 상세 내용의 길이를 제한
    private String detail;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) //외래키 제약 조건 추가
    private Reservation reservation;

    @Builder
    public Review(String grade, String detail, Reservation reservation) {
        this.grade = grade;
        this.detail = detail;
        this.reservation = reservation;
    }


//    public void updateReview(int rating, String detail, String images) {
//        this.detail = detail;
//        this.images = images;
//        // updatedTime은 @LastModifiedDate 어노테이션에 의해 자동 설정 됨
//    }
}