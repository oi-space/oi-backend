package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    private String fileUrl;  // 파일 URL

    @Column(nullable = false)
    private String filePath; // 파일 경로

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review; // 역방향 참조

    @Builder
    public ReviewImage(String fileName, String fileUrl, String filePath, Review review) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.filePath = filePath;
        this.review = review;
    }


    public ReviewImage(String fileUrl) {
        this.fileUrl = fileUrl;

    }
}
