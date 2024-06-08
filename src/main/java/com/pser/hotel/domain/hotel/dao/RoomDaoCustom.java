package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.dto.request.RoomSearchRequest;
import com.pser.hotel.domain.hotel.dto.response.RoomListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomDaoCustom {
    Page<RoomListResponse> search(RoomSearchRequest request, Pageable pageable);
}
