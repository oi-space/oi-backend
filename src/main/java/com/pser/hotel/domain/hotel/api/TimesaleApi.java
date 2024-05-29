package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.TimesaleService;
import com.pser.hotel.domain.hotel.dto.HotelSummaryResponse;
import com.pser.hotel.domain.hotel.dto.TimesaleCreateRequest;
import com.pser.hotel.global.common.response.ApiResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timesales")
public class TimesaleApi {
    public final TimesaleService timesaleService;

    @PostMapping // 타임특가 등록 api
    @PreAuthorize("@methodAuthorizationManager.isTimesaleByIdAndRequest(#userId, #timesaleCreateRequest)")
    public ResponseEntity<ApiResponse<Void>> saveTimesale(@RequestBody TimesaleCreateRequest timesaleCreateRequest,
                                                          @RequestHeader("user-id") long userId) {
        Long timesaleId = timesaleService.saveTimesaleData(timesaleCreateRequest);
        return ResponseEntity.created(URI.create("/timesales/" + timesaleId)).build();
    }

    @GetMapping // 타임특가 숙소 전체 조회 api
    public ResponseEntity<ApiResponse<Slice<HotelSummaryResponse>>> getAllTimesaleHotel(
            @PageableDefault Pageable pageable) {
        Slice<HotelSummaryResponse> timesaleHotelResponses = timesaleService.getAllTimesaleHotelData(pageable);
        return ResponseEntity.ok(ApiResponse.success(timesaleHotelResponses));
    }

    @DeleteMapping("/{timesaleId}") // 타임특가 삭제 api
    @PreAuthorize("@methodAuthorizationManager.isTimesaleByIdAndTimesaleId(#userId, #timesaleId)")
    public ResponseEntity<ApiResponse<Void>> deleteTimesale(@PathVariable Long timesaleId,
                                                            @RequestHeader("user-id") long userId) {
        timesaleService.deleteTimesaleData(timesaleId);
        return ResponseEntity.noContent().build();
    }
}
