package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.RoomResponse;
import com.pser.hotel.domain.hotel.dto.RoomSearchRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDao extends JpaRepository<Room, Long>, RoomDaoCustom {
    @Override
    Page<RoomResponse> search(RoomSearchRequest request, Pageable pageable);

    Optional<Room> findByIdAndHotelId(Long roomId, Long id);
}
