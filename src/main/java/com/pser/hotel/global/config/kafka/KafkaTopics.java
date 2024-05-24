package com.pser.hotel.global.config.kafka;

public interface KafkaTopics {
    String RESERVATION_CREATED = "reservation.created";
    String RESERVATION_PAYMENT_VALIDATION_REQUIRED = "reservation.payment-validation-required";
    String RESERVATION_PAYMENT_VALIDATION_REQUIRED_ROLLBACK = "reservation.payment-validation-required-rollback";
    String RESERVATION_PAYMENT_VALIDATION_CHECKED = "reservation.payment-validation-checked";
    String RESERVATION_REFUND_REQUIRED = "reservation.refund-required";
    String RESERVATION_REFUND_CHECKED = "reservation.refund-checked";
    String AUCTION_CREATED = "auction.created";
    String AUCTION_CREATED_ROLLBACK = "auction.created-rollback";
    String AUCTION_PAID = "auction.paid";
    String AUCTION_NO_BID = "auction.no-bid";
    String AUCTION_BID_REFUSAL = "auction.bid-refusal";
    String AUCTION_PAYMENT_REQUIRED = "auction.payment-required";
}
