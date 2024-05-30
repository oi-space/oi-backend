package com.pser.hotel.domain.hotel.dto.response;

import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelSummaryResponse {
    private Long id;

    private String name;

    private HotelCategoryEnum category;

    private String description;

    private String mainImage;

    private List<String> hotelImageUrls;

    private Double gradeAverage;

    private int salePrice;

    private int previousPrice;

    @QueryProjection
    public HotelSummaryResponse(Long id, String name, HotelCategoryEnum category, String description,
                                String mainImage) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.mainImage = mainImage;
    }
}
