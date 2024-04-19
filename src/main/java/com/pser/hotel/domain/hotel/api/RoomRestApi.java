package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.RoomService;
import com.pser.hotel.domain.hotel.dto.RoomRequestDto;
import com.pser.hotel.domain.hotel.dto.RoomResponseDto;
import com.pser.hotel.domain.hotel.dto.RoomSearchRequest;
import com.pser.hotel.global.common.response.ApiResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rooms")
@RestController
@RequiredArgsConstructor
public class RoomRestApi {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RoomResponseDto>>> roomList(@PageableDefault Pageable pageable) {
        Page<RoomResponseDto> result = roomService.findRoomList(pageable);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> roomDetails(@PathVariable Long roomId) {
        RoomResponseDto result = roomService.findRoom(roomId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RoomResponseDto>>> searchRoom(
            RoomSearchRequest request, @PageableDefault Pageable pageable) {
        Page<RoomResponseDto> result = roomService.search(request, pageable);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> roomSave(@RequestBody RoomRequestDto request,
                                                      @RequestHeader("user-id") long userId) {
        Long roomId = roomService.save(userId, request);
        return ResponseEntity.created(URI.create("/rooms/" + roomId)).build();
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Void>> roomUpdate(@RequestBody RoomRequestDto dto, @PathVariable Long roomId,
                                                        @RequestHeader("user-id") long userId) {
        roomService.update(userId, roomId, dto);
        return ResponseEntity.ok().build();
    }
}
