package com.pser.hotel.domain.hotel.kafka.producer;

import com.pser.hotel.domain.hotel.dto.HotelDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotelStatusProducer {
    private final KafkaTemplate<String, HotelDto> hotelDtoValueKafkaTemplate;

    public void onCreated(HotelDto hotelDto) {
        hotelDtoValueKafkaTemplate.send(KafkaTopics.HOTEL_CREATED, hotelDto);
    }

    public void onUpdated(HotelDto hotelDto) {
        hotelDtoValueKafkaTemplate.send(KafkaTopics.HOTEL_UPDATED, hotelDto);
    }

    public void onDeleted(HotelDto hotelDto) {
        hotelDtoValueKafkaTemplate.send(KafkaTopics.HOTEL_DELETED, hotelDto);
    }
}
