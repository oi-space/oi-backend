package com.pser.hotel.domain.hotel.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName; // 파일명
    private String filePath; // 파일 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review; // 역방향 참조

    @Builder // 빌더 패턴을 사용하기 위한 어노테이션 추가
    public ReviewImage(String fileName, String filePath, Review review) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.review = review;
    }
}
