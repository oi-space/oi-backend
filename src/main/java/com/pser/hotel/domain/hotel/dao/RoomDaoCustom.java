package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.dto.RoomResponseDto;
import com.pser.hotel.domain.hotel.dto.RoomSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomDaoCustom {
    Page<RoomResponseDto> search(RoomSearchRequest request, Pageable pageable);
}
