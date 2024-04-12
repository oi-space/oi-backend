package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.model.BaseEntity;
import java.time.LocalTime;

public class Room extends BaseEntity {
    //TODO: 호텔 엔티티와 병합하면 객체 타입으로 변경
    private String hotel;
    private String name;
    //TODO: Enum으로 구현할 것
    private String category;
    private String description;
    private String precaution;
    private Integer price;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private Integer standardCapacity;
    private Integer maxCapacity;
    private Integer totalRooms;
}
