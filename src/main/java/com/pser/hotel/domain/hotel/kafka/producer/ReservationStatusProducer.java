package com.pser.hotel.domain.hotel.kafka.producer;

import com.pser.hotel.domain.hotel.dto.ReservationDto;
import com.pser.hotel.global.common.PaymentDto;
import com.pser.hotel.global.common.RefundDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationStatusProducer {
    private final KafkaTemplate<String, ReservationDto> reservationDtoValueKafkaTemplate;
    private final KafkaTemplate<String, PaymentDto> paymentDtoValueKafkaTemplate;
    private final KafkaTemplate<String, RefundDto> refundDtoValueKafkaTemplate;

    public void produceCreated(ReservationDto reservationDto) {
        reservationDtoValueKafkaTemplate.send(KafkaTopics.RESERVATION_CREATED, reservationDto);
    }

    public void produceUpdated(ReservationDto reservationDto) {
        reservationDtoValueKafkaTemplate.send(KafkaTopics.RESERVATION_UPDATED, reservationDto);
    }

    public void producePaymentValidationRequired(PaymentDto paymentDto) {
        paymentDtoValueKafkaTemplate.send(KafkaTopics.RESERVATION_PAYMENT_VALIDATION_REQUIRED, paymentDto);
    }

    public void produceRefundRequired(RefundDto refundDto) {
        refundDtoValueKafkaTemplate.send(KafkaTopics.RESERVATION_REFUND_REQUIRED, refundDto);
    }
}
