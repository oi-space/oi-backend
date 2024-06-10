package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.dto.request.ReviewSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewDaoCustom {
    Page<Review> findAllByHotelId(long hotelId, Pageable pageable);

    Page<Review> search(ReviewSearchRequest request, Pageable pageable);

    Slice<Review> findAllByReservationId(long reservationId, Long idAfter, Pageable pageable);

    Slice<Review> findAllByUserId(long userId, Long idAfter, Pageable pageable);

    Page<Review> findAllByRoomId(long roomId, Pageable pageable);

    Page<Review> findAllByRoomName(String roomName, Pageable pageable);
}
