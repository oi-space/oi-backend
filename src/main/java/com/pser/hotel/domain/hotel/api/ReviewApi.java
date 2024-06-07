package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.ReviewService;
import com.pser.hotel.domain.hotel.dto.request.ReviewCreateRequest;
import com.pser.hotel.domain.hotel.dto.request.ReviewSearchRequest;
import com.pser.hotel.domain.hotel.dto.request.ReviewUpdateRequest;
import com.pser.hotel.domain.hotel.dto.response.ReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/reservations/{reservationId}/reviews")
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewService reviewService;

    // 전체 리뷰를 페이지네이션으로 조회합니다.
    @GetMapping
    public ResponseEntity<Page<ReviewResponse>> getAll(Pageable pageable, @PathVariable Long reservationId) {
        return ResponseEntity.ok(reviewService.getAllByReservationId(reservationId, pageable));
    }

    // 특정 ID의 리뷰를 조회합니다.
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getById(@PathVariable Long reservationId, @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getByIdAndReservationId(id, reservationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found with id: " + id)));
    }

    // 새로운 리뷰를 생성합니다. 성공 시 생성된 리뷰의 ID를 반환합니다.
    @PostMapping
    public ResponseEntity<Long> save(@PathVariable Long reservationId, @Valid @RequestBody ReviewCreateRequest request) {
        Long id = reviewService.save(reservationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    // 특정 ID의 리뷰를 업데이트합니다.
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long reservationId, @PathVariable Long id, @Valid @RequestBody ReviewUpdateRequest request) {
        reviewService.update(reservationId, id, request);
        return ResponseEntity.ok().build();
    }

    // 특정 ID의 리뷰를 삭제합니다. 성공 시 메시지를 반환합니다.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long reservationId, @PathVariable Long id) {
        reviewService.delete(reservationId, id);
        return ResponseEntity.noContent().build();
    }

    // 예외 처리를 위한 ExceptionHandler 추가
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getReason());
    }
}
