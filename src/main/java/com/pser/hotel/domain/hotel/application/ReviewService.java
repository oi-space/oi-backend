package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.ReviewDao;
import com.pser.hotel.domain.hotel.domain.Review;
import com.pser.hotel.domain.hotel.dto.mapper.ReviewMapper;
import com.pser.hotel.domain.hotel.dto.request.ReviewCreateRequest;
import com.pser.hotel.domain.hotel.dto.request.ReviewSearchRequest;
import com.pser.hotel.domain.hotel.dto.request.ReviewUpdateRequest;
import com.pser.hotel.domain.hotel.dto.response.ReviewResponse;
import com.pser.hotel.global.common.request.SearchQuery;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewDao reviewDao;
    private final ReviewMapper reviewMapper;

    public Page<ReviewResponse> getAll(Pageable pageable) {
        return reviewDao.findAll(pageable).map(reviewMapper::toResponse);
    }

    public Page<ReviewResponse> search(ReviewSearchRequest request, SearchQuery searchQuery, Pageable pageable) {
        return reviewDao.search(request, pageable).map(reviewMapper::toResponse);
    }

    public ReviewResponse getById(Long id) {
        Review review = findById(id);
        return reviewMapper.toResponse(review);
    }

    public Long save(Long reservationId, ReviewCreateRequest request) {
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
    public void delete(Long id, Long aLong) {
        Review review = findById(id);
        reviewDao.delete(review);
    }

    private Review findById(Long id) {
        Optional<Review> review = reviewDao.findById(id);
        if (review.isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
        return review.get();
    }

    public Page<ReviewResponse> getAllByReservationId(Long reservationId, Pageable pageable) {
        return reviewDao.findByReservationId(reservationId, pageable).map(reviewMapper::toResponse);
    }

    public Optional<ReviewResponse> getByIdAndReservationId(Long id, Long reservationId) {
        return reviewDao.findByIdAndReservationId(id, reservationId)
                .map(reviewMapper::toResponse);
    }
}
