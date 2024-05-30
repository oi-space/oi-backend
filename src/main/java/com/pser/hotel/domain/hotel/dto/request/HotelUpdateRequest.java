package com.pser.hotel.domain.hotel.dto.request;

import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@AllArgsConstructor
public class HotelUpdateRequest {
    private String name;

    private HotelCategoryEnum category;

    private String description;

    private String notice;

    private String province;

    private String city;

    private String district;

    private String detailedAddress;

    private Double latitude;

    private Double longitude;

    @URL(message = "URL 형식이 유효하지 않습니다.")
    private String mainImage;

    private String businessNumber;

    @URL(message = "URL 형식이 유효하지 않습니다.")
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
}
