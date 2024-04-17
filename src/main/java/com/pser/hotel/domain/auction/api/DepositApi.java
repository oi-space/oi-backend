package com.pser.hotel.domain.auction.api;

import com.pser.hotel.domain.auction.dto.DepositCreateRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions/{auctionId}/users/{userId}/deposit")
@RequiredArgsConstructor
public class DepositApi {
    @PostMapping
    @PreAuthorize("#authId == #userId")
    public ResponseEntity<Void> save(@RequestHeader("User-Id") long authId,
                                     @PathVariable long auctionId,
                                     @PathVariable long userId,
                                     @Validated @RequestBody DepositCreateRequest request) {
        //TODO: 서비스, DAO 구현 필요
        //TODO: 간편 결제 승인 로직 tid 이용하여 구현
        long id = 1L;
        return ResponseEntity.created(URI.create("/auctions/%d/users/%d/deposit".formatted(auctionId, userId))).build();
    }

    @DeleteMapping
    @PreAuthorize("#authId == #userId")
    public ResponseEntity<Void> delete(@RequestHeader("User-Id") long authId,
                                       @PathVariable long auctionId,
                                       @PathVariable long userId) {
        //TODO: 서비스, DAO 구현 필요
        //TODO: 간편 결제 취소 로직 tid 이용하여 구현
        return ResponseEntity.noContent().build();
    }
}
