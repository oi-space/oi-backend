package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.ReservationService;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationCreateRequest;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationFindResponseDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationResponse;
import com.pser.hotel.global.common.response.ApiResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationApi {
    private final ReservationService reservationService;

    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getById(@PathVariable long reservationId) {
        ReservationResponse response = reservationService.getById(reservationId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ReservationFindResponseDto> findAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam("userEmail") String userEmail) {
        return ResponseEntity.ok(reservationService.findAllByUserEmail(page, userEmail));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestHeader("User-Id") long authId,
                                     @Validated @RequestBody ReservationCreateRequest reservationCreateRequest) {
        reservationCreateRequest.setAuthId(authId);
        long id = reservationService.save(reservationCreateRequest);
        return ResponseEntity.created(URI.create("/reservations/%s".formatted(id))).build();
    }

    @PutMapping
    public ResponseEntity<ReservationUpdateResponseDto> update(
            @Validated @RequestBody ReservationUpdateRequestDto reservationUpdateRequestDto) {
        return ResponseEntity.ok(reservationService.update(reservationUpdateRequestDto));
    }

    @DeleteMapping
    public ResponseEntity<ReservationDeleteResponseDto> delete(@RequestParam(name = "roomName") String roomName) {
        return new ResponseEntity(reservationService.delete(roomName), HttpStatus.OK);
    }
}
