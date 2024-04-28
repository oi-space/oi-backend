package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelDao extends JpaRepository<Hotel, Long>, HotelDaoCustom {
}
