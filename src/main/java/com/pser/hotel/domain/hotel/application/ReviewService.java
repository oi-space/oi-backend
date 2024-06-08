package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.ReservationDao;
import com.pser.hotel.domain.hotel.dao.ReviewDao;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.dto.mapper.ReviewMapper;
import com.pser.hotel.domain.hotel.dto.request.ReviewCreateRequest;
import com.pser.hotel.domain.hotel.dto.request.ReviewUpdateRequest;
import com.pser.hotel.domain.hotel.dto.response.ReviewResponse;
import com.pser.hotel.domain.member.dao.ProfileDao;
import com.pser.hotel.domain.member.domain.Profile;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;
    private final ReservationDao reservationDao;
    private final ProfileDao profileDao;
    private final ReviewMapper reviewMapper;

    @Transactional
    public Long save(ReviewCreateRequest request) {
        Reservation reservation = reservationDao.findById(request.getReservationId())
                .orElseThrow();
        Profile profile = profileDao.findById(reservation.getUser().getId())
                .orElseThrow();
        request.setReservation(reservation);
        request.setProfileImageUrl(profile.getImageUrl());
        request.setReviewerName(profile.getUsername());
        request.setHotelId(reservation.getRoom().getHotel().getId());

        Review review = reviewMapper.toEntity(request);
        review = reviewDao.save(review);
        return review.getId();
    }

    @Transactional
    public void update(Long id, Long aLong, ReviewUpdateRequest request) {
        Review review = findById(id);
        reviewMapper.updateReviewFromDto(request, review);
        reviewDao.save(review);
    }

    @Transactional
    public void delete(Long id) {
        Review review = findById(id);
        reviewDao.delete(review);
    }

    public Slice<ReviewResponse> getAllByReservationId(Long reservationId, Long idAfter, Pageable pageable) {
        return reviewDao.findAllByReservationId(reservationId, idAfter, pageable)
                .map(reviewMapper::toResponse);
    }

    public Slice<ReviewResponse> getAllByUserId(Long userId, Long idAfter, Pageable pageable) {
        return reviewDao.findAllByReservationId(userId, idAfter, pageable)
                .map(reviewMapper::toResponse);
    }

    public Optional<ReviewResponse> getByIdAndReservationId(Long id, Long reservationId) {
        return reviewDao.findByIdAndReservationId(id, reservationId)
                .map(reviewMapper::toResponse);
    }

    private Review findById(Long id) {
        Optional<Review> review = reviewDao.findById(id);
        if (review.isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
        return review.get();
    }
}
