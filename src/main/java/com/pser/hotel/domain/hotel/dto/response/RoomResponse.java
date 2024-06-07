package com.pser.hotel.domain.hotel.dto.response;

import com.pser.hotel.domain.hotel.domain.Amenity;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {

    private Long id;

    private String name;

    private String description;

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

    @QueryProjection
    public RoomResponse(String name, String description, String precaution, int price, LocalTime checkIn,
                        LocalTime checkOut, int standardCapacity, int maxCapacity, int totalRooms, Amenity amenity) {
        this.name = name;
        this.description = description;
        this.precaution = precaution;
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.standardCapacity = standardCapacity;
        this.maxCapacity = maxCapacity;
        this.totalRooms = totalRooms;
        this.heatingSystem = amenity.getHeatingSystem();
        this.tv = amenity.getTv();
        this.refrigerator = amenity.getRefrigerator();
        this.airConditioner = amenity.getAirConditioner();
        this.washer = amenity.getWasher();
        this.terrace = amenity.getTerrace();
        this.coffeeMachine = amenity.getCoffeeMachine();
        this.internet = amenity.getInternet();
        this.kitchen = amenity.getKitchen();
        this.bathtub = amenity.getBathtub();
        this.iron = amenity.getIron();
        this.pool = amenity.getPool();
        this.pet = amenity.getPet();
        this.inAnnex = amenity.getInAnnex();
    }
}
