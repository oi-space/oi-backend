package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.dto.request.ReservationCreateRequest;

public interface ReservationDaoCustom {
    int countOverlappingReservations(ReservationCreateRequest request);
}
