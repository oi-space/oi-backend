package com.pser.hotel.domain.hotel.kafka.consumer;

import com.pser.hotel.domain.hotel.application.ReservationService;
import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import com.pser.hotel.domain.hotel.dto.response.ReservationResponse;
import com.pser.hotel.global.common.PaymentDto;
import com.pser.hotel.global.common.StatusUpdateDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import com.pser.hotel.global.error.SameStatusException;
import com.pser.hotel.global.error.ValidationFailedException;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationRefundCheckedConsumer {
    private final ReservationService reservationService;

    @RetryableTopic(kafkaTemplate = "paymentDtoValueKafkaTemplate", attempts = "5")
    @KafkaListener(topics = KafkaTopics.RESERVATION_REFUND_CHECKED, groupId = "${kafka.consumer-group-id}", containerFactory = "paymentDtoValueListenerContainerFactory")
    public void onRefundChecked(PaymentDto paymentDto) {
        Try.run(() -> updateStatus(paymentDto))
                .recover(SameStatusException.class, (e) -> null)
                .recover(ValidationFailedException.class, (e) -> null)
                .get();
    }

    private void updateStatus(PaymentDto paymentDto) {
        ReservationResponse reservationResponse = reservationService.getByMerchantUid(paymentDto.getMerchantUid());
        ReservationStatusEnum status = reservationResponse.getStatus();
        ReservationStatusEnum targetStatus = getNextStatus(status);

        StatusUpdateDto<ReservationStatusEnum> statusUpdateDto = StatusUpdateDto.<ReservationStatusEnum>builder()
                .merchantUid(paymentDto.getMerchantUid())
                .targetStatus(targetStatus)
                .build();
        reservationService.updateStatus(statusUpdateDto);
    }

    private ReservationStatusEnum getNextStatus(ReservationStatusEnum status) {
        if (status.equals(ReservationStatusEnum.BID_PAYBACK_REQUIRED)) {
            return ReservationStatusEnum.AUCTION_SUCCESS;
        } else if (status.equals(ReservationStatusEnum.DEPOSIT_PAYBACK_REQUIRED)) {
            return ReservationStatusEnum.AUCTION_FAILURE;
        }
        return ReservationStatusEnum.REFUNDED;
    }
}
