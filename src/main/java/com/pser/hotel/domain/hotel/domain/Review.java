package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import com.pser.hotel.domain.model.GradeEnum;
import com.pser.hotel.domain.model.GradeEnumConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(indexes = {@Index(name = "idx_review_reservation_id", columnList = "reservation_id")})
public class Review extends BaseEntity {
    @Column(nullable = false)
    @Convert(converter = GradeEnumConverter.class)
    private GradeEnum grade;

    @Length(min = 10, max = 500)
    private String detail;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(nullable = false, unique = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reservation reservation;

    @Column(nullable = false)
    private Long hotelId;

    @Column(nullable = false)
    private String reviewerName;

    @URL
    private String profileImageUrl;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Room room;

    @Column(nullable = false)
    private String roomName;

    @Builder
    public Review(GradeEnum grade, String detail, Reservation reservation, Long hotelId, String reviewerName,
                  String profileImageUrl, List<ReviewImage> reviewImages, Room room, String roomName) {
        this.grade = grade;
        this.detail = detail;
        this.reservation = reservation;
        this.hotelId = hotelId;
        this.reviewerName = reviewerName;
        this.profileImageUrl = profileImageUrl;
        this.reviewImages = reviewImages != null ? reviewImages : new ArrayList<>();
        this.room = room;
        this.roomName = roomName;
    }

    public void addReviewImageFile(ReviewImage reviewImage) {
        this.reviewImages.add(reviewImage);
        reviewImage.setReview(this);
    }
}
