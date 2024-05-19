package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.Amenity;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class TimesaleRoomResponse {
    private long id;
    private long hotelId;
    private String name;
    private String description;
    private String precaution;
    private int previousPrice;
    private int salePrice;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private int standardCapacity;
    private int maxCapacity;
    private int totalRooms;
    private Amenity amenity;
}
