package com.pser.hotel.domain.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelRequest {
    @NotBlank
    private Long userId;
    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @NotBlank
    private String description;
    @NotBlank
    private String notice;
    @NotBlank
    private String province;
    @NotBlank
    private String city;
    @NotBlank
    private String district;
    @NotBlank
    private String detailedAddress;
    @NotBlank
    private Double latitude;
    @NotBlank
    private Double longtitude;
    @NotBlank
    private String mainImage;
    @NotBlank
    private String businessNumber;;
    @NotBlank
    private String certUrl;
    @NotBlank
    private String visitGuidance;
    @NotBlank
    private Boolean parkingLot;
    @NotBlank
    private Boolean wifi;
    @NotBlank
    private Boolean barbecue;
}
