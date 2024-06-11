package com.pser.hotel.domain.hotel.kafka.consumer;

import com.pser.hotel.domain.hotel.application.ReservationService;
import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import com.pser.hotel.global.common.StatusUpdateDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationPaymentValidationRequiredRollbackConsumer {
    private final ReservationService reservationService;

    @RetryableTopic(kafkaTemplate = "stringValueKafkaTemplate", attempts = "5", retryTopicSuffix = "-retry-${kafka.consumer-group-id}")
    @KafkaListener(topics = KafkaTopics.RESERVATION_PAYMENT_VALIDATION_REQUIRED_ROLLBACK, groupId = "${kafka.consumer-group-id}", containerFactory = "stringValueListenerContainerFactory")
    public void rollbackPaymentValidationRequired(String merchantUid) {
        StatusUpdateDto<ReservationStatusEnum> statusUpdateDto = StatusUpdateDto.<ReservationStatusEnum>builder()
                .merchantUid(merchantUid)
                .targetStatus(ReservationStatusEnum.CREATED)
                .build();
        reservationService.rollbackStatus(statusUpdateDto);
    }
}
