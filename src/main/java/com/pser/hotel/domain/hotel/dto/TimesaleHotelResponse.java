package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private Double gradeAverage;

    private int salePrice;

    private int previousPrice;

    @QueryProjection
    public TimesaleHotelResponse(Long id, String name, HotelCategoryEnum category, String description,
                         String notice, String province, String city, String district, String detailedAddress,
                         Double latitude,
                         Double longtitude, String mainImage, String businessNumber, String certUrl,
                         String visitGuidance,
                         Boolean parkingLot, Boolean wifi, Boolean barbecue, Boolean sauna, Boolean swimmingPool,
                         Boolean restaurant,
                         Boolean roofTop, Boolean fitness, Boolean dryer, Boolean breakfast, Boolean smokingArea,
                         Boolean allTimeDesk,
                         Boolean luggageStorage, Boolean snackBar, Boolean petFriendly) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.notice = notice;
        this.province = province;
        this.city = city;
        this.district = district;
        this.detailedAddress = detailedAddress;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.mainImage = mainImage;
        this.businessNumber = businessNumber;
        this.certUrl = certUrl;
        this.visitGuidance = visitGuidance;
        this.parkingLot = parkingLot;
        this.wifi = wifi;
        this.barbecue = barbecue;
        this.sauna = sauna;
        this.swimmingPool = swimmingPool;
        this.restaurant = restaurant;
        this.roofTop = roofTop;
        this.fitness = fitness;
        this.dryer = dryer;
        this.breakfast = breakfast;
        this.smokingArea = smokingArea;
        this.allTimeDesk = allTimeDesk;
        this.luggageStorage = luggageStorage;
        this.snackBar = snackBar;
        this.petFriendly = petFriendly;
    }
}
