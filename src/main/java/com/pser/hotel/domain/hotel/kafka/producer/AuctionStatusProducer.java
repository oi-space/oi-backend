package com.pser.hotel.domain.hotel.kafka.producer;

import com.pser.hotel.global.common.AuctionDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionStatusProducer {
    private final KafkaTemplate<String, AuctionDto> auctionDtoValueKafkaTemplate;

    public void rollbackCreated(AuctionDto auctionDto) {
        auctionDtoValueKafkaTemplate.send(KafkaTopics.AUCTION_CREATED_ROLLBACK, auctionDto);
    }
}
