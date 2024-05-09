package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationSaveRequestDto;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationUpdateRequestDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationDeleteResponseDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationFindResponseDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationUpdateResponseDto;
import com.pser.hotel.domain.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationApi {
    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ReservationFindResponseDto> findAll(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam("userEmail") String userEmail){
        return ResponseEntity.ok(reservationService.findAllByUserEmail(page, userEmail));
    }
    @PostMapping("/save")
    public ResponseEntity<ReservationSaveRequestDto> save(@RequestBody ReservationSaveRequestDto reservationSaveRequestDto){
        return new ResponseEntity(reservationService.save(reservationSaveRequestDto), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<ReservationUpdateResponseDto> update(@RequestBody ReservationUpdateRequestDto reservationUpdateRequestDto){
        return ResponseEntity.ok(reservationService.update(reservationUpdateRequestDto));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ReservationDeleteResponseDto> delete(@RequestParam(name = "roomName") String roomName){
        return new ResponseEntity(reservationService.delete(roomName), HttpStatus.OK);
    }
}
