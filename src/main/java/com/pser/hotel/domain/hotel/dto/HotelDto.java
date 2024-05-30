package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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

    private String mainImage;

    private String businessNumber;

    private String certUrl;

    private String visitGuidance;

    private Long userId;

    private FacilityDto facility;

    private List<String> images;

    private List<RoomDto> rooms;
}
