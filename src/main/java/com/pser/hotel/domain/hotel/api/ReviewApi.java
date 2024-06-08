package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.ReviewService;
import com.pser.hotel.domain.hotel.dto.response.ReviewResponse;
import com.pser.hotel.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{userId}/reviews")
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<Slice<ReviewResponse>>> getAll(Pageable pageable,
                                                                     @PathVariable Long userId,
                                                                     Long idAfter) {
        Slice<ReviewResponse> result = reviewService.getAllByUserId(userId, idAfter, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
