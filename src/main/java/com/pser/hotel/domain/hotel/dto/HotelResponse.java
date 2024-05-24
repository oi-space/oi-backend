package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private List<String> hotelImageUrls;

    private Double gradeAverage;

    private int salePrice;

    private int previousPrice;

    private LocalDateTime createdAt;

    @QueryProjection
    public HotelResponse(Long id, String name, String description,
                         String city, String district, String detailedAddress,
                         String mainImage, String visitGuidance, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.district = district;
        this.detailedAddress = detailedAddress;
        this.mainImage = mainImage;
        this.visitGuidance = visitGuidance;
        this.createdAt = createdAt;
    }
}
