package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Facility;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityDao extends JpaRepository<Facility, Long> {
    Optional<Facility> findByHotelId(Long hotelId);

    void deleteByHotelId(Long hotelId);
}
