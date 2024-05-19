package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Hotel;
import java.util.Optional;

public interface TimesaleCustom {
    Optional<Hotel> findHotelByRoomId(Long roomId);

    Optional<Hotel> findHotelByTimesaleId(Long timesaleId);
}
