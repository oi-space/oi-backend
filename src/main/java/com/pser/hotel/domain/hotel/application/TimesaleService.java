package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.dao.TimesaleDao;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import com.pser.hotel.domain.hotel.dto.TimesaleCreateRequest;
import com.pser.hotel.domain.hotel.dto.TimesaleHotelResponse;
import com.pser.hotel.domain.hotel.dto.TimesaleMapper;
import com.pser.hotel.domain.hotel.dto.TimesaleRoomResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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

    @Transactional // 같은 객실인데 겹치는 시간대에 특가를 넣으면 에러가 터지게 수정해야함
    public Long saveTimesaleData(TimesaleCreateRequest timesaleCreateRequest) {
        Room room = roomDao.findById(timesaleCreateRequest.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found room"));
        TimeSale timeSale = timesaleMapper.changeToTimesale(timesaleCreateRequest, room);

        timesaleDao.save(timeSale);
        return timeSale.getId();
    }

    public Slice<TimesaleHotelResponse> getAllTimesaleHotelData(Pageable pageable) {
        List<TimeSale> timeSaleNowList = validateNowTimesale();
        List<TimesaleRoomResponse> timesaleRoomList = validateTimesaleRoom(timeSaleNowList);
        List<TimesaleHotelResponse> timesaleHotelList = validateTimesaleHotel(timesaleRoomList);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), timesaleHotelList.size());
        List<TimesaleHotelResponse> subList = timesaleHotelList.subList(start, end);

        boolean hasNext = end < timesaleHotelList.size();

        return new SliceImpl<>(subList, pageable, hasNext);
    }

    @Transactional
    public void deleteTimesaleData(Long timesaleId) {
        TimeSale timeSale = timesaleDao.findById(timesaleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found timesale"));

        timesaleDao.delete(timeSale);
    }

    private List<TimeSale> validateNowTimesale() {
        List<TimeSale> timeSaleAllList = timesaleDao.findAll();
        List<TimeSale> timeSaleNowList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (TimeSale timesale : timeSaleAllList) {
            if (timesale.getStartAt().isBefore(now) && timesale.getEndAt().isAfter(now)) {
                timeSaleNowList.add(timesale);
            }
        }
        return timeSaleNowList;
    }

    private List<TimesaleRoomResponse> validateTimesaleRoom(List<TimeSale> timeSaleNowList) {
        List<TimesaleRoomResponse> roomList = new ArrayList<>();
        for (TimeSale timeSale : timeSaleNowList) {
            Room room = roomDao.findById(timeSale.getRoom().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found room"));
            TimesaleRoomResponse timesaleRoomResponse = timesaleMapper.changeToTimesaleRoomResponse(room,
                    timeSale.getPrice());
            roomList.add(timesaleRoomResponse);
        }
        return roomList;
    }

    private List<TimesaleHotelResponse> validateTimesaleHotel(List<TimesaleRoomResponse> timeSaleRoomList) {
        List<TimesaleHotelResponse> hotelList = new ArrayList<>();
        for (TimesaleRoomResponse room : timeSaleRoomList) {
            Hotel hotel = hotelDao.findById(room.getHotelId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found hotel"));
            TimesaleHotelResponse timesaleHotelResponse = timesaleMapper.changeToTimeHotelResponse(hotel,
                    room.getPreviousPrice(),
                    room.getSalePrice());
            // 저장된 타임특가 객실 중 호텔이 동일한 경우 현재 갖고온 타임특가 객실가보다 리스트에 저장된 타임특가 객실가보다 싸다면 이를 제거하고 timesaleHotelResponse가 들어간다
            boolean isMoreCheap = true;
            for (TimesaleHotelResponse savedTimesaleHotel : hotelList) {
                if (timesaleHotelResponse.getName().equals(savedTimesaleHotel.getName())) {
                    if (timesaleHotelResponse.getSalePrice() < savedTimesaleHotel.getSalePrice()) {
                        hotelList.remove(savedTimesaleHotel);
                    } else {
                        isMoreCheap = false;
                    }
                    break;
                }
            }
            if (isMoreCheap) {
                hotelList.add(timesaleHotelResponse);
            }
        }
        return hotelList;
    }
}
