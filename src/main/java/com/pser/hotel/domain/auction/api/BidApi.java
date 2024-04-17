package com.pser.hotel.domain.auction.api;

import com.pser.hotel.domain.auction.dto.BidResponse;
import com.pser.hotel.global.common.response.ApiResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions/{auctionId}/bids")
@RequiredArgsConstructor
public class BidApi {
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BidResponse>>> getAll(@PathVariable long auctionId,
                                                                 @PageableDefault Pageable pageable) {
        Page<BidResponse> result = null;
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestHeader("User-Id") long authId, @PathVariable long auctionId) {
        //TODO: 서비스, DAO 구현 필요
        //TODO: 서비스에서 보증금 결제 여부 확인 필요
        long id = 1L;
        return ResponseEntity.created(URI.create("/auctions/%d/bids/%d".formatted(auctionId, id))).build();
    }
}
