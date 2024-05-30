package com.pser.hotel.domain.hotel.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDto {
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long hotelId;

    private Boolean parkingLot;

    private Boolean barbecue;

    private Boolean wifi;

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
}
