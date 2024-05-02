package com.pser.hotel.global.config;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dto.RoomRequest;
import com.pser.hotel.global.error.UserNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("methodAuthorizationManager")
@RequiredArgsConstructor
public class MethodAuthorizationManager {
    private final HotelDao hotelDao;

    public boolean isHotelByIdAndRequest(Long userId, RoomRequest request) {
        hotelDao.findByIdAndUserId(request.getHotelId(), userId)
                .orElseThrow(() -> new UserNotAllowedException());
        return true;
    }

    public boolean isHotelByIdAndUserId(Long userId, Long hotelId) {
        hotelDao.findByIdAndUserId(hotelId, userId)
                .orElseThrow(() -> new UserNotAllowedException());
        return true;
    }
}
