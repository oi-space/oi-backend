package com.pser.hotel.domain.hotel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomResponseDto {
    private String name;

    public RoomResponseDto(String name) {
        this.name = name;
    }
}
