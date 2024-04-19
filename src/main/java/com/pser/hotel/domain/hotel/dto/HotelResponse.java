package com.pser.hotel.domain.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HotelResponse {
    private Long id;

    private Long userId;

    private String name;

    private String category;

    private String description;

    private String notice;

    private String province;

    private String city;

    private String district;

    private String detailedAddress;

    private Double latitude;

    private Double longtitude;

    private String mainImage;

    private String businessNumber;

    private String certUrl;

    private String visitGuidance;

    private Boolean parkingLot;

    private Boolean wifi;

    private Boolean barbecue;
}
