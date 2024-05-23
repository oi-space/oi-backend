package com.pser.hotel.global.config.kafka;

public interface KafkaTopics {
    String RESERVATION_CREATED = "reservation.created";
    String AUCTION_CREATED = "auction.created";
    String AUCTION_CREATED_ROLLBACK = "auction.created-rollback";
}
