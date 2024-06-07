package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.RoomService;
import com.pser.hotel.domain.hotel.dto.response.RoomResponse;
import com.pser.hotel.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rooms")
@RestController
@RequiredArgsConstructor
public class RoomRestApi {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RoomResponse>>> roomList(@PageableDefault Pageable pageable) {
        Page<RoomResponse> result = roomService.findRoomList(pageable);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponse>> roomDetails(@PathVariable(name = "roomId") long roomId) {
        RoomResponse result = roomService.findRoom(roomId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
