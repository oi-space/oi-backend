package com.pser.hotel.global.config;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.dao.TimesaleDao;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.dto.request.TimesaleCreateRequest;
import com.pser.hotel.global.error.UserNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component("methodAuthorizationManager")
@RequiredArgsConstructor
public class MethodAuthorizationManager {
    private final HotelDao hotelDao;
    private final TimesaleDao timesaleDao;
    private final RoomDao roomDao;

    public boolean isHotelByIdAndUserId(Long userId, Long hotelId) {
        hotelDao.findByIdAndUserId(hotelId, userId)
                .orElseThrow(() -> new UserNotAllowedException());
        return true;
    }

    public boolean isTimesaleByIdAndRequest(Long userId, TimesaleCreateRequest timesaleCreateRequest) {
        Hotel hotel = timesaleDao.findHotelByRoomId(timesaleCreateRequest.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found hotel"));
        return hotel.getUser().getId().equals(userId);
    }

    public boolean isTimesaleByIdAndTimesaleId(Long userId, Long timesaleId) {
        Hotel hotel = timesaleDao.findHotelByTimesaleId(timesaleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found hotel"));
        return hotel.getUser().getId().equals(userId);
    }
}
