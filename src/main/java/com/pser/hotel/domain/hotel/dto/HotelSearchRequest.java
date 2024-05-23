package com.pser.hotel.domain.hotel.dto;


import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.global.common.request.SearchQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelSearchRequest extends SearchQuery {

    private String name;

    private HotelCategoryEnum category;

    private String province;

    private String city;

    private String district;

    private String detailedAddress;

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

    private Integer people;

    private LocalDate searchStartAt;

    private LocalDate searchEndAt;

    @Builder
    public HotelSearchRequest(String keyword, LocalDateTime createdAfter, LocalDateTime createdBefore,
                              LocalDateTime updatedAfter, LocalDateTime updatedBefore, String name,
                              HotelCategoryEnum category, String province,
                              String city, String district, String detailedAddress, Boolean parkingLot, Boolean wifi,
                              Boolean barbecue, Boolean sauna, Boolean swimmingPool,
                              Boolean restaurant, Boolean roofTop, Boolean fitness, Boolean dryer, Boolean breakfast,
                              Boolean smokingArea,
                              Boolean allTimeDesk, Boolean luggageStorage, Boolean snackBar, Boolean petFriendly,
                              Integer people, LocalDate searchStartAt, LocalDate searchEndAt) {
        super(keyword, createdAfter, createdBefore, updatedAfter, updatedBefore);
        this.name = name;
        this.category = category;
        this.province = province;
        this.city = city;
        this.district = district;
        this.detailedAddress = detailedAddress;
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
        this.people = people;
        this.searchStartAt = searchStartAt;
        this.searchEndAt = searchEndAt;
    }
}
