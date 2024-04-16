package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.dto.RoomResponseDto;
import com.pser.hotel.global.common.response.ApiResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class RoomApi {
    // TODO : RoomService DI 예정

    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse<Page<RoomResponseDto>>> roomList(@PageableDefault Pageable pageable){
        List<RoomResponseDto> fetch = List.of();
        long count = 0;
        Page<RoomResponseDto> result = new PageImpl<>(fetch, pageable,count);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto> roomDetails(@PathVariable Long roomId){
        RoomResponseDto res = new RoomResponseDto();
        return ResponseEntity.ok(ApiResponse.success(res));
    }
}
