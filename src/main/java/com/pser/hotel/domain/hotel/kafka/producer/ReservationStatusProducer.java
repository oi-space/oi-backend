package com.pser.hotel.domain.hotel.kafka.producer;

import com.pser.hotel.domain.hotel.dto.ReservationDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationStatusProducer {
    private final KafkaTemplate<String, ReservationDto> reservationDtoValueKafkaTemplate;

    public void produceCreated(ReservationDto reservationDto) {
        reservationDtoValueKafkaTemplate.send(KafkaTopics.RESERVATION_CREATED, reservationDto);
    }
}
