package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.request.RoomSearchRequest;
import com.pser.hotel.domain.hotel.dto.response.RoomListResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RoomDao extends JpaRepository<Room, Long>, RoomDaoCustom {
    @Override
    Page<RoomListResponse> search(RoomSearchRequest request, Pageable pageable);

    Optional<Room> findByName(@Param("name") String name);

    Optional<Room> findByIdAndHotelId(Long roomId, Long hotelId);
}
