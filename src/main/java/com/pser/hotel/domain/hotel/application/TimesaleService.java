package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.dao.TimesaleDao;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import com.pser.hotel.domain.hotel.dto.TimesaleCreateRequest;
import com.pser.hotel.domain.hotel.dto.TimesaleHotelResponse;
import com.pser.hotel.domain.hotel.dto.TimesaleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TimesaleService {
    public final TimesaleMapper timesaleMapper;
    public final TimesaleDao timesaleDao;
    public final RoomDao roomDao;
    public final HotelDao hotelDao;

    @Transactional
    public Long saveTimesaleData(TimesaleCreateRequest timesaleCreateRequest) {

        if (!timesaleDao.checkTimesaleTimeIsValid(timesaleCreateRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "overlaps timesale period of the same room");
        }

        Room room = roomDao.findById(timesaleCreateRequest.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found room"));
        TimeSale timeSale = timesaleMapper.changeToTimesale(timesaleCreateRequest, room);

        timesaleDao.save(timeSale);
        return timeSale.getId();
    }

    public Slice<TimesaleHotelResponse> getAllTimesaleHotelData(Pageable pageable) {
        return timesaleDao.findNowTimesaleHotel(pageable);
    }

    @Transactional
    public void deleteTimesaleData(Long timesaleId) {
        TimeSale timeSale = timesaleDao.findById(timesaleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found timesale"));

        timesaleDao.delete(timeSale);
    }
}
