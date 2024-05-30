package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.Column;
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
public class ReviewImage extends BaseEntity {

    private String fileName; // 파일명

    @Column(nullable = false) // nullable 속성 추가
    private String filePath; // 파일 경로

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // optional 속성 추가
    @JoinColumn(name = "review_id", nullable = false) // review_id가 null을 허용하지 않도록 설정
    private Review review; // 역방향 참조

    @Builder // 빌더 패턴 추가
    public ReviewImage(String fileName, String filePath, Review review) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.review = review;
    }

    public String getUrl() {
        // 일단 웹서버에 저장된다고 가정 한 코드
        return "http://yourserver.com/files/" + this.filePath;
    }
}
