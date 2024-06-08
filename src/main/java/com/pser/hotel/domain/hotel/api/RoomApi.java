package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.RoomService;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import com.pser.hotel.domain.hotel.dto.response.RoomDetailResponse;
import com.pser.hotel.domain.hotel.dto.response.RoomListResponse;
import com.pser.hotel.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<ApiResponse<Page<RoomListResponse>>> roomList(@PathVariable long hotelId,
                                                                        @PageableDefault Pageable pageable) {
        Page<RoomListResponse> result = roomService.findRoomList(hotelId, pageable);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<ApiResponse<RoomDetailResponse>> roomDetails(@PathVariable(name = "hotelId") long hotelId,
                                                                       @PathVariable(name = "roomId") long roomId) {
        RoomDetailResponse result = roomService.findRoom(hotelId, roomId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/hotels/{hotelId}/rooms")
    @PreAuthorize("@methodAuthorizationManager.isHotelByIdAndUserId(#userId, #hotelId)")
    public ResponseEntity<ApiResponse<Long>> roomSave(@RequestBody RoomRequest request,
                                                      @PathVariable long hotelId,
                                                      @RequestHeader("user-id") long userId) {
        Long saveRoomId = roomService.save(userId, hotelId, request);
        return ResponseEntity.ok(ApiResponse.success(hotelId));
    }

    @PatchMapping("/hotels/{hotelId}/rooms/{roomId}")
    @PreAuthorize("@methodAuthorizationManager.isHotelByIdAndUserId(#userId, #hotelId)")
    public ResponseEntity<ApiResponse<Void>> roomUpdate(@RequestBody RoomRequest request,
                                                        @PathVariable Long hotelId,
                                                        @PathVariable Long roomId,
                                                        @RequestHeader("user-id") long userId) {
        roomService.update(userId, hotelId, roomId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/hotels/{hotelId}/rooms/{roomId}")
    @PreAuthorize("@methodAuthorizationManager.isHotelByIdAndUserId(#userId, #hotelId)")
    public ResponseEntity<ApiResponse<Void>> roomDelete(@PathVariable long hotelId,
                                                        @PathVariable long roomId,
                                                        @RequestHeader("user-id") long userId) {
        roomService.remove(userId, hotelId, roomId);
        return ResponseEntity.noContent().build();
    }
}
