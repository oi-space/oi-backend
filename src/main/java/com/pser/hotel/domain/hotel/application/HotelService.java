package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.FacilityDao;
import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.UserDao;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.dto.HotelCreateRequest;
import com.pser.hotel.domain.hotel.dto.HotelMapper;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.HotelUpdateRequest;
import com.pser.hotel.domain.member.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelDao hotelDao;
    private final FacilityDao facilityDao;
    private final UserDao userDao;
    private final HotelMapper hotelMapper;

    public Slice<HotelResponse> getAllHotelData(Pageable pageable) {
        return hotelDao.findAll(pageable).map(hotelMapper::changeToHotelResponse);
    }

    public Optional<HotelResponse> getHotelDataById(Long id) {
        return hotelDao.findById(id).map(hotelMapper::changeToHotelResponse);
    }

    public Slice<HotelResponse> searchHotelData(HotelSearchRequest hotelSearchRequest,
        Pageable pageable) {
        return hotelDao.search(hotelSearchRequest, pageable);
    }

    @Transactional
    public Long saveHotelData(HotelCreateRequest hotelCreateRequest, Long userId) {
        User user = userDao.findById(userId)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "not found user id"));

        Hotel hotel = hotelMapper.changeToHotel(hotelCreateRequest, user);
        Facility facility = hotelMapper.changeToFacility(hotelCreateRequest, hotel);

        hotelDao.save(hotel);

        return hotel.getId();
    }

    @Transactional
    public void updateHotelData(HotelUpdateRequest hotelUpdateRequest, Long hotelId) {
        Hotel hotel = hotelDao.findById(hotelId)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "not found hotel"));
        Facility facility = facilityDao.findByHotelId(hotelId)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "not found facility"));

        hotelMapper.updateHotelFromDto(hotelUpdateRequest, hotel);
        hotelMapper.updateFacilityFromDto(hotelUpdateRequest, facility);

        hotelDao.save(hotel);
    }

    @Transactional
    public void deleteHotelData(Long hotelId) {
        Hotel hotel = hotelDao.findById(hotelId)
            .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "not found hotel"));

        hotelDao.delete(hotel);
    }
}