package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import com.pser.hotel.domain.model.GradeEnum;
import com.pser.hotel.domain.model.GradeEnumConverter;
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

    @Column(nullable = false)
    @Convert(converter = GradeEnumConverter.class)
    private GradeEnum grade;

    @Length(min = 10, max = 500) // 상세 내용의 길이를 제한
    private String detail;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false, unique = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reservation reservation;
    @Builder
    public Review(GradeEnum grade, String detail, Reservation reservation) {
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