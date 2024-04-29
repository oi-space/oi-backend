package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.dto.RoomResponse;
import com.pser.hotel.domain.hotel.dto.RoomSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomDaoCustom {
    Page<RoomResponse> search(RoomSearchRequest request, Pageable pageable);
}
