package com.pser.hotel.domain.hotel.kafka.consumer;

import com.pser.hotel.domain.hotel.application.ReservationService;
import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationResponse;
import com.pser.hotel.domain.hotel.kafka.producer.ReservationStatusProducer;
import com.pser.hotel.global.common.AuctionDto;
import com.pser.hotel.global.common.RefundDto;
import com.pser.hotel.global.common.StatusUpdateDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import com.pser.hotel.global.error.SameStatusException;
import com.pser.hotel.global.error.StatusUpdateException;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionBidRefusalConsumer {
    private final ReservationService reservationService;
    private final ReservationStatusProducer reservationStatusProducer;

    @RetryableTopic(kafkaTemplate = "auctionDtoValueKafkaTemplate", attempts = "5")
    @KafkaListener(topics = KafkaTopics.AUCTION_BID_REFUSAL, groupId = "${kafka.consumer-group-id}", containerFactory = "auctionDtoValueListenerContainerFactory")
    public void onAuctionBidRefusal(AuctionDto auctionDto) {
        long reservationId = auctionDto.getReservationId();
        Try.run(() -> {
                    StatusUpdateDto<ReservationStatusEnum> statusUpdateDto = StatusUpdateDto.<ReservationStatusEnum>builder()
                            .id(reservationId)
                            .targetStatus(ReservationStatusEnum.DEPOSIT_PAYBACK_REQUIRED)
                            .build();
                    reservationService.updateStatus(statusUpdateDto);
                })
                .onSuccess(unused -> refundEqualToDeposit(auctionDto))
                .recover(SameStatusException.class, (e) -> null)
                .recover(StatusUpdateException.class, (e) -> null)
                .get();
    }

    private void refundEqualToDeposit(AuctionDto auctionDto) {
        long reservationId = auctionDto.getReservationId();
        int refundAmount = auctionDto.getDepositPrice();
        ReservationResponse reservationResponse = reservationService.getById(reservationId);
        RefundDto refundDto = RefundDto.builder()
                .impUid(reservationResponse.getImpUid())
                .merchantUid(reservationResponse.getMerchantUid())
                .amount(refundAmount)
                .build();
        reservationStatusProducer.produceRefundRequired(refundDto);
    }
}
