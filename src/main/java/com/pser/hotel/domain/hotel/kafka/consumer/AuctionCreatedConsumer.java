package com.pser.hotel.domain.hotel.kafka.consumer;

import com.pser.hotel.domain.hotel.application.ReservationService;
import com.pser.hotel.domain.hotel.kafka.producer.AuctionStatusProducer;
import com.pser.hotel.global.common.AuctionDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
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
public class AuctionCreatedConsumer {
    private final ReservationService reservationService;
    private final AuctionStatusProducer auctionStatusProducer;

    @RetryableTopic(kafkaTemplate = "auctionDtoValueKafkaTemplate", attempts = "5")
    @KafkaListener(topics = KafkaTopics.AUCTION_CREATED, groupId = "${kafka.consumer-group-id}", containerFactory = "auctionDtoValueListenerContainerFactory")
    public void onCreated(AuctionDto auctionDto) {
        long reservationId = auctionDto.getReservationId();
        Try.run(() -> reservationService.updateToAuctionOngoingStatus(reservationId))
                .recover(StatusUpdateException.class, (e) -> {
                    auctionStatusProducer.rollbackCreated(auctionDto);
                    return null;
                })
                .get();
    }
}
