package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@AllArgsConstructor
public class HotelCreateRequest {
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
    private Double longitude;
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
    @NotBlank
    private Boolean sauna;
    @NotBlank
    private Boolean swimmingPool;
    @NotBlank
    private Boolean restaurant;
    @NotBlank
    private Boolean roofTop;
    @NotBlank
    private Boolean fitness;
    @NotBlank
    private Boolean dryer;
    @NotBlank
    private Boolean breakfast;
    @NotBlank
    private Boolean smokingArea;
    @NotBlank
    private Boolean allTimeDesk;
    @NotBlank
    private Boolean luggageStorage;
    @NotBlank
    private Boolean snackBar;
    @NotBlank
    private Boolean petFriendly;

    private List<String> hotelImageUrls;
}
