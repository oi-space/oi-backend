package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.HotelImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelImageDao extends JpaRepository<HotelImage, Long> {
    List<HotelImage> findByHotelId(Long hotelId);

    Optional<HotelImage> findByImageUrl(String imageUrl);
}
