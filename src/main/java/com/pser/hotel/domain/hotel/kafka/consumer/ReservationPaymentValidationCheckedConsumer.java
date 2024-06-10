package com.pser.hotel.domain.hotel.kafka.consumer;

import com.pser.hotel.domain.hotel.application.ReservationService;
import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import com.pser.hotel.domain.hotel.kafka.producer.ReservationStatusProducer;
import com.pser.hotel.global.common.PaymentDto;
import com.pser.hotel.global.common.StatusUpdateDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import com.pser.hotel.global.error.SameStatusException;
import com.pser.hotel.global.error.ValidationFailedException;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationPaymentValidationCheckedConsumer {
    private final ReservationService reservationService;
    private final ReservationStatusProducer reservationStatusProducer;

    @RetryableTopic(kafkaTemplate = "paymentDtoValueKafkaTemplate", attempts = "5")
    @KafkaListener(topics = KafkaTopics.RESERVATION_PAYMENT_VALIDATION_CHECKED, groupId = "${kafka.consumer-group-id}", containerFactory = "paymentDtoValueListenerContainerFactory")
    public void updateToPaymentValidationChecked(PaymentDto paymentDto) {
        Try.run(() -> check(paymentDto))
                .recover(SameStatusException.class, (Void) null)
                .recover(ValidationFailedException.class, (e) -> refund(paymentDto))
                .get();
    }

    @DltHandler
    public void dltHandler(ConsumerRecord<String, PaymentDto> record) {
        Try.run(() -> {
                    PaymentDto paymentDto = record.value();
                    refund(paymentDto);
                }).recover(Exception.class, e -> {
                    log.error("dlt failed by error: " + e.getMessage());
                    return null;
                })
                .get();
    }

    private void check(PaymentDto paymentDto) {
        StatusUpdateDto<ReservationStatusEnum> statusUpdateDto = StatusUpdateDto.<ReservationStatusEnum>builder()
                .merchantUid(paymentDto.getMerchantUid())
                .targetStatus(ReservationStatusEnum.BEFORE_CHECKIN)
                .build();
        reservationService.updateStatus(statusUpdateDto, reservation -> {
            int paidAmount = paymentDto.getAmount();
            if (reservation.getPrice() != paidAmount) {
                throw new ValidationFailedException("결제 금액 불일치");
            }
        });
    }

    private Void refund(PaymentDto paymentDto) {
        reservationService.refund(paymentDto.getMerchantUid());
        return null;
    }
}
