package com.pser.hotel.domain.hotel.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long hotelId;

    private String name;

    private String description;

    private String mainImageUrl;

    private String precaution;

    private Integer price;

    private LocalTime checkIn;

    private LocalTime checkOut;

    private Integer standardCapacity;

    private Integer maxCapacity;

    private Integer totalRooms;

    private AmenityDto amenity;

    private List<String> roomImages;
}
