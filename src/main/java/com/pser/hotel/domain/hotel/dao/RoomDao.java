package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomDao extends JpaRepository<Room, Long>, RoomDaoCustom {
}
