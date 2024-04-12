package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.model.BaseEntity;
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
@EntityListeners(AuditingEntityListener.class)
public class Hotel extends BaseEntity {
    private String name;
    private String category;
    //    TODO : Enum 구현 예정
    private String description;
    private String notice;
    private String province;
    private String city;
    private String district;
    private String detailedAddress;
    private double latitude;
    private double longtitude;
    private String mainImage;
    private String businessNumber;
    private String certUrl;
    private String visitGuidance;
}
