package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.dto.RoomRequestDto;
import com.pser.hotel.domain.hotel.dto.RoomResponseDto;
import com.pser.hotel.domain.hotel.dto.RoomSearchRequest;
import com.pser.hotel.global.common.response.ApiResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rooms")
@RestController
public class RoomApi {
    // TODO : RoomService DI 예정

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RoomResponseDto>>> roomList(@PageableDefault Pageable pageable) {
        List<RoomResponseDto> fetch = List.of();
        long count = 0;
        Page<RoomResponseDto> result = new PageImpl<>(fetch, pageable, count);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> roomDetails(@PathVariable Long roomId) {
        RoomResponseDto res = new RoomResponseDto("TEST_NAME");
        return ResponseEntity.ok(ApiResponse.success(res));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RoomResponseDto>>> searchRoom(
            RoomSearchRequest request, @PageableDefault Pageable pageable) {
        List<RoomResponseDto> fetch = List.of();
        long count = 0;
        Page<RoomResponseDto> result = new PageImpl<>(fetch, pageable, count);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> roomSave(@RequestBody RoomRequestDto request, Long userId) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Void>> roomUpdate(@RequestBody RoomRequestDto dto, @PathVariable Long roomId,
                                                        Long userId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Void>> roomDelete(@PathVariable Long roomId, Long userId) {
        return ResponseEntity.ok().build();
    }

}
