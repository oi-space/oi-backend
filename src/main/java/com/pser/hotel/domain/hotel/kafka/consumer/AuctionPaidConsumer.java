package com.pser.hotel.domain.hotel.kafka.consumer;

import com.pser.hotel.domain.hotel.application.ReservationService;
import com.pser.hotel.domain.hotel.dto.response.ReservationResponse;
import com.pser.hotel.domain.hotel.kafka.producer.ReservationStatusProducer;
import com.pser.hotel.global.common.AuctionDto;
import com.pser.hotel.global.common.RefundDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionPaidConsumer {
    private final ReservationStatusProducer reservationStatusProducer;
    private final ReservationService reservationService;

    @RetryableTopic(kafkaTemplate = "auctionDtoValueKafkaTemplate", attempts = "5", retryTopicSuffix = "-retry-${kafka.consumer-group-id}")
    @KafkaListener(topics = KafkaTopics.AUCTION_PAID, groupId = "${kafka.consumer-group-id}", containerFactory = "auctionDtoValueListenerContainerFactory")
    public void onAuctionPaid(AuctionDto auctionDto) {
        Try.run(() -> refundEqualToBid(auctionDto))
                .get();
    }

    private void refundEqualToBid(AuctionDto auctionDto) {
        long reservationId = auctionDto.getReservationId();
        int refundAmount = auctionDto.getEndPrice();
        ReservationResponse reservationResponse = reservationService.getById(reservationId);
        RefundDto refundDto = RefundDto.builder()
                .impUid(reservationResponse.getImpUid())
                .merchantUid(reservationResponse.getMerchantUid())
                .amount(refundAmount)
                .build();
        reservationStatusProducer.produceRefundRequired(refundDto);
    }
}
