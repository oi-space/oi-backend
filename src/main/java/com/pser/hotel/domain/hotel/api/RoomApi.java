package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.RoomService;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import com.pser.hotel.global.common.response.ApiResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomApi {
    private final RoomService roomService;

    @PostMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<ApiResponse<Void>> roomSave(@RequestBody RoomRequest request,
                                                      @PathVariable(name = "hotelId") long hotelId,
                                                      @RequestHeader("user-id") long userId) {
        Long saveRoomId = roomService.save(userId, hotelId, request);
        return ResponseEntity.created(URI.create("/rooms/" + saveRoomId)).build();
    }

    @PatchMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<ApiResponse<Void>> roomUpdate(@RequestBody RoomRequest request,
                                                        @PathVariable(name = "hotelId") Long hotelId,
                                                        @PathVariable(name = "roomId") Long roomId,
                                                        @RequestHeader("user-id") long userId) {
        roomService.update(userId, hotelId, roomId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<ApiResponse<Void>> roomDelete(@PathVariable(name = "hotelId") long hotelId,
                                                        @PathVariable(name = "roomId") long roomId,
                                                        @RequestHeader("user-id") long userId) {
        roomService.remove(userId, hotelId, roomId);
        return ResponseEntity.noContent().build();
    }
}
