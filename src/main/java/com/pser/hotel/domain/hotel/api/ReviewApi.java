package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.dto.ReviewRequestDto;
import com.pser.hotel.domain.hotel.dto.ReviewResponseDto;
import com.pser.hotel.domain.hotel.dto.ReviewListResponseDto;
import com.pser.hotel.global.common.response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/reviews")
@RestController
public class ReviewApi {

    @GetMapping
    public ResponseEntity<ApiResponse<ReviewListResponseDto>> reviewList(@PageableDefault Pageable pageable) {

        List<ReviewListResponseDto.ReviewDto> fetch = List.of(); // 임시 데이터
        long totalReviews = 0; // 임시 데이터
        ReviewListResponseDto result = ReviewListResponseDto.builder()
                .reviews(fetch)
                .totalReviews(totalReviews)
                .build();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createReview(@RequestBody ReviewRequestDto request) {
        // 리뷰 생성 후 생성된 리뷰 ID 반환
        Long createdReviewId = 1L; // 임시 데이터

        return ResponseEntity.created(URI.create("/reviews/" + createdReviewId))
                .body(ApiResponse.success(createdReviewId));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> reviewDetails(@PathVariable Long reviewId) {
        //  리뷰 상세 정보 조회
        ReviewResponseDto res = ReviewResponseDto.builder().id(reviewId).build(); // 임시 데이터

        return ResponseEntity.ok(ApiResponse.success(res));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> updateReview(@PathVariable Long reviewId,
                                                          @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok().build();
    }
}
