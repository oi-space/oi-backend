package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
    private Long id;

    private Long userId;

    private String name;

    private HotelCategoryEnum category;

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

    public HotelResponse(Hotel hotel) {
        this.id = hotel.getId();
        this.userId = hotel.getUser().getId();
        this.name = hotel.getName();
        this.category = hotel.getCategory();
        this.description = hotel.getDescription();
        this.notice = hotel.getNotice();
        this.province = hotel.getProvince();
        this.city = hotel.getCity();
        this.district = hotel.getDistrict();
        this.detailedAddress = hotel.getDetailedAddress();
        this.latitude = hotel.getLatitude();
        this.longtitude = hotel.getLongtitude();
        this.mainImage = hotel.getMainImage();
        this.businessNumber = hotel.getBusinessNumber();
        this.certUrl = hotel.getCertUrl();
        this.visitGuidance = hotel.getVisitGuidance();
        this.parkingLot = hotel.getFacility().getParkingLot();
        this.wifi = hotel.getFacility().getWifi();
        this.barbecue = hotel.getFacility().getBarbecue();
    }
}
