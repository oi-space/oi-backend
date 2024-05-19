package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimesaleHotelResponse {
    private Long id;

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

    private Boolean sauna;

    private Boolean swimmingPool;

    private Boolean restaurant;

    private Boolean roofTop;

    private Boolean fitness;

    private Boolean dryer;

    private Boolean breakfast;

    private Boolean smokingArea;

    private Boolean allTimeDesk;

    private Boolean luggageStorage;

    private Boolean snackBar;

    private Boolean petFriendly;

    private int previousPrice;

    private int salePrice;
}
