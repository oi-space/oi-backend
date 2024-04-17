package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Review extends BaseEntity {
    private String grade;

    @Length(min = 10, max = 500) // 상세 내용의 길이를 제한
    private String detail;

    private String rentId;

    private String images; // 이미지는 유효성 검사 없이 저장


    private String nickname;

    private int rating;

    @CreatedDate
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime; // 글 생성 날짜를 저장하는 필드

    @LastModifiedDate
    @Column(name = "updated_time")
    private LocalDateTime updatedTime; // 글 수정 날짜를 저장하는 필드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel; // 리뷰가 속한 호텔

    @Column(name = "is_deleted")
    private boolean isDeleted = false; // 삭제 여부를 나타내는 필드

    // 생성자
    public Review(int rating, String grade, Hotel hotel, String detail, String rentId, String images, String nickname) {
        this.rating = rating;
        this.grade = grade;
        this.detail = detail;
        this.rentId = rentId;
        this.images = images;
        this.nickname = nickname;
        this.hotel = hotel;
        // createdTime과 updatedTime은 각각 @CreatedDate, @LastModifiedDate 어노테이션에 의해 자동 설정 됨.
    }

    public void updateReview(int rating, String detail, String images) {
        this.rating = rating;
        this.detail = detail;
        this.images = images;
        // updatedTime은 @LastModifiedDate 어노테이션에 의해 자동 설정 됨
    }

    public void deleteReview() {
        this.isDeleted = true;
    }
}