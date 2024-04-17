package com.pser.hotel.domain.hotel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelResponse {
    private String response;

    public HotelResponse(String response){
        this.response = response;
    }
}
