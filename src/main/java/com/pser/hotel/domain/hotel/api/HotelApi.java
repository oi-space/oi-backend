package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.dto.HotelCreateRequest;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.global.common.response.ApiResponse;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotels")
public class HotelApi {

    @PostMapping // 숙소 등록 api
    public ResponseEntity<ApiResponse<Void>> saveHotel(@RequestBody HotelCreateRequest hotelCreateRequest, @RequestHeader("user-id") Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping // 숙소 전체 조회
    public ResponseEntity<ApiResponse<Slice<HotelResponse>>> getAllHotel(@PageableDefault Pageable pageable){

        List<HotelResponse> list = Collections.emptyList();
        Slice<HotelResponse> result = new SliceImpl<>(list, pageable, false);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/search") // 숙소 검색 조회
    public ResponseEntity<ApiResponse<Slice<HotelResponse>>> searchHotel(HotelSearchRequest hotelSearchRequest, @PageableDefault Pageable pageable){

        List<HotelResponse> list = Collections.emptyList();
        Slice<HotelResponse> result = new SliceImpl<>(list, pageable, false);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{hotelId}") // 특정 숙소 조회
    public ResponseEntity<ApiResponse<HotelResponse>> getHotel(@PathVariable Long hotelId){
        HotelResponse hotelRequest = new HotelResponse("first controller");
        return ResponseEntity.ok(ApiResponse.success(hotelRequest));
    }

    @PatchMapping("/{hotelId}") // 숙소 수정
    public ResponseEntity<ApiResponse<Void>> updateHotel(HotelCreateRequest hotelCreateRequest, @PathVariable Long hotelId, @RequestHeader("user-id") Long userId){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{hotelId}") // 숙소 삭제
    public ResponseEntity<ApiResponse<Void>> deleteHotel(HotelCreateRequest hotelCreateRequest, @PathVariable Long hotelId, @RequestHeader("user-id") Long userId){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
