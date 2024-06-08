package com.pser.hotel.domain.hotel.dto.response;

import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDetailResponse {

    private Long id;

    private Long hotelId;

    private String name;

    private String description;

    private String mainImageUrl;

    private String precaution;

    private int price;

    private LocalTime checkIn;

    private LocalTime checkOut;

    private int standardCapacity;

    private int maxCapacity;

    private int totalRooms;

    private Boolean heatingSystem = false;

    private Boolean tv = false;

    private Boolean refrigerator = false;

    private Boolean airConditioner = false;

    private Boolean washer = false;

    private Boolean terrace = false;

    private Boolean coffeeMachine = false;

    private Boolean internet = false;

    private Boolean kitchen = false;

    private Boolean bathtub = false;

    private Boolean iron = false;

    private Boolean pool = false;

    private Boolean pet = false;

    private Boolean inAnnex = false;

    private List<String> roomImages;
}
