package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface HotelDaoCustom {
    Slice<HotelResponse> search(HotelSearchRequest hotelSearchRequest, Pageable pageable);

    double getHotelGrade(Long hotelId);

    Slice<HotelResponse> findAllWithGradeAndPrice(Pageable pageable);

    HotelResponse findHotel(Long hotelId);
}
