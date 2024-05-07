package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.HotelService;
import com.pser.hotel.domain.hotel.dto.HotelCreateRequest;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.HotelUpdateRequest;
import com.pser.hotel.global.common.response.ApiResponse;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelApi {

    private final HotelService hotelService;

    @PostMapping // 숙소 등록 api
    public ResponseEntity<ApiResponse<Void>> saveHotel(@RequestBody HotelCreateRequest hotelCreateRequest, @RequestHeader("user-id") long userId) {
        Long savedUserId = hotelService.saveHotelData(hotelCreateRequest, userId);
        return ResponseEntity.created(URI.create("/hotels/" + savedUserId)).build();
    }

    @GetMapping // 숙소 전체 조회
    public ResponseEntity<ApiResponse<Slice<HotelResponse>>> getAllHotel(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(hotelService.getAllHotelData(pageable)));
    }

    @GetMapping("/search") // 숙소 검색 조회
    public ResponseEntity<ApiResponse<Slice<HotelResponse>>> searchHotel(HotelSearchRequest hotelSearchRequest, @PageableDefault Pageable pageable){
        Slice<HotelResponse> result = hotelService.searchHotelData(hotelSearchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{hotelId}") // 특정 숙소 조회
    public ResponseEntity<ApiResponse<Optional<HotelResponse>>> getHotel(@PathVariable Long hotelId){
        return ResponseEntity.ok(ApiResponse.success(hotelService.getHotelDataById(hotelId)));
    }

    @PatchMapping("/{hotelId}") // 숙소 수정
    public ResponseEntity<ApiResponse<Void>> updateHotel(@RequestBody HotelUpdateRequest hotelUpdateRequest, @PathVariable Long hotelId, @RequestHeader("user-id") long userId){
        Long updatedHotelId = hotelService.updateHotelData(hotelUpdateRequest, hotelId, userId);
        return ResponseEntity.created(URI.create("hotels/" + updatedHotelId)).build();
    }

    @DeleteMapping("/{hotelId}") // 숙소 삭제
    public ResponseEntity<ApiResponse<Void>> deleteHotel(@PathVariable Long hotelId, @RequestHeader("user-id") long userId){
        Long deletedHotelId = hotelService.deleteHotelData(hotelId, userId);
        return ResponseEntity.created(URI.create("hotels/" + deletedHotelId)).build();
    }
}
