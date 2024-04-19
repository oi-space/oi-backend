package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.Amenity;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class RoomResponseDto {
    private String name;
    private String description;
    private String precaution;
    private int price;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private int standardCapacity;
    private int maxCapacity;
    private int totalRooms;
    private Amenity amenity;

    @QueryProjection
    public RoomResponseDto(String name, String description, String precaution, int price, LocalTime checkIn,
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
        this.amenity = amenity;
    }
}
