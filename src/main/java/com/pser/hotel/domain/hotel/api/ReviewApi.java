package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.dto.ReviewListResponseDto;
import com.pser.hotel.domain.hotel.dto.ReviewRequest;
import com.pser.hotel.domain.hotel.dto.ReviewResponseDto;
import com.pser.hotel.global.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/reviews")
@RestController
public class ReviewApi {
    // TODO : ReviewService DI 예정

    @GetMapping
    public ResponseEntity<ApiResponse<ReviewListResponseDto>> reviewList(Pageable pageable) {
        // pageable 기반으로 실제 리뷰 리스트 조회 로직
        // 임시 데이터, PageImpl 객체 생성
        Page<ReviewListResponseDto.ReviewDto> reviewPage = new PageImpl<>(List.of()); // 임시

        ReviewListResponseDto result = new ReviewListResponseDto(reviewPage);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody ReviewRequest request) {
        Long createdReviewId = 1L;

        URI location = URI.create("/reviews/" + createdReviewId);
        return ResponseEntity.created(location).build();
    }
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> reviewDetails(@PathVariable Long reviewId) {
        // 리뷰 상세 정보 조회
        ReviewResponseDto res = ReviewResponseDto.builder().id(reviewId).build();

        return ResponseEntity.ok(ApiResponse.success(res));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> updateReview(@PathVariable Long reviewId,
                                                          @RequestBody ReviewRequest dto) {
        // 리뷰 수정
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId) {
        // 리뷰 삭제
        return ResponseEntity.ok().build();
    }
}
