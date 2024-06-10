package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.dto.request.ReviewSearchRequest;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface ReviewDao extends JpaRepository<Review, Long>, ReviewDaoCustom {
    @Override
    @NonNull
    Page<Review> findAll(@NonNull Pageable pageable);

    Page<Review> search(ReviewSearchRequest dto, @NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<Review> findById(@NonNull Long id);

    Optional<Review> findByIdAndReservationId(Long id, Long reservationId); // 반환 타입을 Optional<Review>로 변경

    Page<Review> findByRoomId(@NonNull Long roomId, @NonNull Pageable pageable);

    Page<Review> findByRoomName(@NonNull String roomName, @NonNull Pageable pageable);

    Page<Review> findByHotelId(@NonNull Long hotelId, @NonNull Pageable pageable);

    Page<Review> findByReviewerName(@NonNull String reviewerName, @NonNull Pageable pageable);

    Page<Review> findByProfileImageUrl(@NonNull String profileImageUrl, @NonNull Pageable pageable);

    Page<Review> findByRoomIdAndRoomName(@NonNull Long roomId, @NonNull String roomName, @NonNull Pageable pageable);

}
