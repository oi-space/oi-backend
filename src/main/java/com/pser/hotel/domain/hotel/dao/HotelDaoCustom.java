package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.dto.response.HotelResponse;
import com.pser.hotel.domain.hotel.dto.request.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.response.HotelSummaryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface HotelDaoCustom {
    Slice<HotelSummaryResponse> search(HotelSearchRequest hotelSearchRequest, Pageable pageable);

    double getHotelGrade(Long hotelId);

    Slice<HotelSummaryResponse> findAllWithGradeAndPrice(Pageable pageable);

    HotelResponse findHotel(Long hotelId);
}
