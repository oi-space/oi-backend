package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.ReviewService;
import com.pser.hotel.domain.hotel.dto.request.ReviewCreateRequest;
import com.pser.hotel.domain.hotel.dto.request.ReviewUpdateRequest;
import com.pser.hotel.domain.hotel.dto.response.ReviewResponse;
import com.pser.hotel.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/reservations/{reservationId}/reviews")
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getAll(Pageable pageable,
                                                                    @PathVariable Long reservationId) {
        Page<ReviewResponse> result = reviewService.getAllByReservationId(reservationId, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getById(@PathVariable Long reservationId,
                                                               @PathVariable Long id) {
        ReviewResponse result = reviewService.getByIdAndReservationId(id, reservationId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found with id: " + id));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> save(@PathVariable Long reservationId,
                                                  @Valid @RequestBody ReviewCreateRequest request) {
        Long id = reviewService.save(reservationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long reservationId, @PathVariable Long id,
                                                    @Valid @RequestBody ReviewUpdateRequest request) {
        reviewService.update(reservationId, id, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long reservationId, @PathVariable Long id) {
        reviewService.delete(reservationId, id);
        return ResponseEntity.noContent().build();
    }


}
