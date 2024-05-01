package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.dto.ReviewRequest;
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
    // TODO : ReviewService DI 예정

    @GetMapping
    public ResponseEntity<ApiResponse<ReviewListResponseDto>> reviewList(@PageableDefault Pageable pageable) {
        // 임시 데이터를 사용하여 리뷰 리스트 생성
        List<ReviewListResponseDto.ReviewDto> fetch = List.of();
        long totalReviews = 0;
        ReviewListResponseDto result = ReviewListResponseDto.builder()
                .reviews(fetch)
                .totalReviews(totalReviews)
                .build();

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody ReviewRequest request) {
        // 리뷰 생성 로직 (생략)
        Long createdReviewId = 1L; // 임시 데이터로 생성된 리뷰 ID

        // 생성된 리소스 위치를 'Location' 헤더로 반환
        URI location = URI.create("/reviews/" + createdReviewId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> reviewDetails(@PathVariable Long reviewId) {
        // 리뷰 상세 정보 조회 로직 (생략)
        ReviewResponseDto res = ReviewResponseDto.builder().id(reviewId).build(); // 임시 데이터

        return ResponseEntity.ok(ApiResponse.success(res));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> updateReview(@PathVariable Long reviewId,
                                                          @RequestBody ReviewRequest dto) {
        // 리뷰 수정 로직 (생략)
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId) {
        // 리뷰 삭제 로직 (생략)
        return ResponseEntity.ok().build();
    }
}
