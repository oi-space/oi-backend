package com.pser.hotel.domain.hotel.dto;


import com.pser.hotel.global.common.request.SearchQuery;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelSearchRequest extends SearchQuery {

    private String name;

    private String category;

    private String province;

    private String city;

    private String district;

    private String detailedAddress;

    private Boolean parkingLot;

    private Boolean wifi;

    private Boolean barbecue;

    @Builder
    public HotelSearchRequest(String keyword, LocalDateTime createdAfter, LocalDateTime createdBefore,
        LocalDateTime updatedAfter, LocalDateTime updatedBefore, String name, String category, String province,
        String city, String district, String detailedAddress, Boolean parkingLot, Boolean wifi, Boolean barbecue){
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
    }
}
