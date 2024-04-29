package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.RoomService;
import com.pser.hotel.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomApi {
    private final RoomService roomService;

    @DeleteMapping("/hotels/{hotelId}/rooms/{roomId}")
    @PreAuthorize("@methodAuthorizationManager.isHotelByIdAndUserId(#userId, #hotelId)")
    public ResponseEntity<ApiResponse<Void>> roomDelete(@PathVariable long hotelId, @PathVariable long roomId,
                                                        @RequestHeader("user-id") long userId) {
        roomService.remove(userId, hotelId, roomId);
        return ResponseEntity.noContent().build();
    }
}
