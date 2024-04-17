package com.pser.hotel.domain.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomRequestDto {
    @NotBlank
    private long hotelId;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String precaution;
    @NotBlank
    private int price;
    @NotBlank
    private int standardCapacity;
    @NotBlank
    private int maxCapacity;
    @NotBlank
    private int totalRooms;
    @NotBlank
    private Boolean heatingSystem;
    @NotBlank
    private Boolean tv;
    @NotBlank
    private Boolean refrigerator;
    @NotBlank
    private Boolean airConditioner;
    @NotBlank
    private Boolean washer;
    @NotBlank
    private Boolean terrace;
    @NotBlank
    private Boolean coffeeMachine;
    @NotBlank
    private Boolean internet;
    @NotBlank
    private Boolean kitchen;
    @NotBlank
    private Boolean bathtub;
    @NotBlank
    private Boolean iron;
    @NotBlank
    private Boolean pool;
    @NotBlank
    private Boolean pet;
    @NotBlank
    private Boolean inAnnex;
}
