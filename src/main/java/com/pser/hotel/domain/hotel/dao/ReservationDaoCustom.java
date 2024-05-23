package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationCreateRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReservationDaoCustom {
    int countOverlappingReservations(ReservationCreateRequest request);
}
