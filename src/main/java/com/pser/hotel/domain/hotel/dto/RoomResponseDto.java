package com.pser.hotel.domain.hotel.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class RoomResponseDto {
    private String name;

    @QueryProjection
    public RoomResponseDto(String name) {
        this.name = name;
    }
}
