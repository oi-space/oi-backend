package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@AllArgsConstructor
public class HotelUpdateRequest {
    @NotBlank
    private Long userId;
    @NotBlank
    private String name;
    @NotBlank
    private HotelCategoryEnum category;
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
    @URL(message = "URL 형식이 유효하지 않습니다.")
    private String mainImage;
    @NotBlank
    private String businessNumber;
    @NotBlank
    @URL(message = "URL 형식이 유효하지 않습니다.")
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
