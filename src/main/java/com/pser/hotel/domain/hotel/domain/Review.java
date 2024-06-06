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

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id 필드 추가

    @Column(nullable = false)
    @Convert(converter = GradeEnumConverter.class)
    private GradeEnum grade;

    @Length(min = 10, max = 500)
    private String detail;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false, unique = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reservation reservation;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Builder
    public Review(Long id, GradeEnum grade, String detail, Reservation reservation, List<ReviewImage> reviewImages) {
        this.id = id; // id 필드 추가
        this.grade = grade;
        this.detail = detail;
        this.reservation = reservation;
        this.reviewImages = reviewImages != null ? reviewImages : new ArrayList<>();
    }

    public void addReviewImageFile(ReviewImage reviewImage) {
        this.reviewImages.add(reviewImage);
        reviewImage.setReview(this);
    }
}
