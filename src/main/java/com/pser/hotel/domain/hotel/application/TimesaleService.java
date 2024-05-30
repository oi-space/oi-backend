package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.dao.TimesaleDao;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import com.pser.hotel.domain.hotel.dto.HotelSummaryResponse;
import com.pser.hotel.domain.hotel.dto.TimesaleCreateRequest;
import com.pser.hotel.domain.hotel.dto.TimesaleMapper;
import java.util.List;
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

        if (!checkTimesaleTimeIsValid(timesaleCreateRequest)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "overlaps timesale period of the same room");
        }

        Room room = roomDao.findById(timesaleCreateRequest.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found room"));
        TimeSale timeSale = timesaleMapper.changeToTimesale(timesaleCreateRequest, room);

        timesaleDao.save(timeSale);
        return timeSale.getId();
    }

    public Slice<HotelSummaryResponse> getAllTimesaleHotelData(Pageable pageable) {
        return timesaleDao.findNowTimesaleHotel(pageable);
    }

    @Transactional
    public void deleteTimesaleData(Long timesaleId) {
        TimeSale timeSale = timesaleDao.findById(timesaleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found timesale"));

        timesaleDao.delete(timeSale);
    }

    public boolean checkTimesaleTimeIsValid(TimesaleCreateRequest dto) {
        List<TimeSale> timeSaleList = timesaleDao.findByRoomId(dto.getRoomId());
        if (timeSaleList.isEmpty()) { // 같은 객실 타임특가가 없다면 체킹할 필요 X
            return true;
        }
        for (TimeSale timeSale : timeSaleList) {
            if (timeSale.getStartAt().isAfter(dto.getEndAt()) || timeSale.getEndAt().isBefore(dto.getStartAt())) {
                continue;
            } else {
                // 이미 존재하는 타임특가 중 겹치는 시간이 존재하면 return false;
                return false;
            }
        }
        return true;
    }
}
