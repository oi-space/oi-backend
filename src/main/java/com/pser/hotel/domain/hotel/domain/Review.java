package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Review extends BaseEntity {
    private String grade;

    @Length(min = 10, max = 500) // 상세 내용의 길이를 제한
    private String detail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Reservation reservation;

    @Builder
    public Review(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String grade, String detail, Reservation reservation) {
        super(id, createdAt, updatedAt);
        this.grade = grade;
        this.detail = detail;
        this.reservation = reservation;
    }

    public void setReservation(Reservation reservation){
        if(this.reservation != null){
            // null검사와 비슷한 유효성 검사라고 생각하기.
            // 내가 가지고 있는 Reservertion객체의 List<Review>에서 나(this)를 remove해준다.
        }
        this.reservation = reservation;
        // 넣은 Reservation에 나를 add 추가해준다.
    }

    //    private String images; // 이미지는 유효성 검사 없이 저장


//    public Review(int rating, String grade, Hotel hotel, String detail, String rentId, String images, String nickname) {
//        this.grade = grade;
//        this.detail = detail;
//        this.rentId = rentId;
//        this.images = images;
//        this.hotel = hotel;
//        // createdTime과 updatedTime은 각각 @CreatedDate, @LastModifiedDate 어노테이션에 의해 자동 설정 됨.
//    }

//    public void updateReview(int rating, String detail, String images) {
//        this.detail = detail;
//        this.images = images;
//        // updatedTime은 @LastModifiedDate 어노테이션에 의해 자동 설정 됨
//    }
}