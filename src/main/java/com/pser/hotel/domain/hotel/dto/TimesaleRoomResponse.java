package com.pser.hotel.domain.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimesaleRoomResponse {
    private long id;
    private long hotelId;
    private int previousPrice;
    private int salePrice;
}
