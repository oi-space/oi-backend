package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Hotel extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
    //    TODO : Enum 구현 예정
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String notice;
    @Column(nullable = false)
    private String province;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private String detailedAddress;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longtitude;
    @Column(nullable = false)
    private String mainImage;
    @Column(nullable = false)
    private String businessNumber;
    @Column(nullable = false)
    private String certUrl;
    @Column(nullable = false)
    private String visitGuidance;
}
