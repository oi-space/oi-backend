package com.pser.hotel.domain.hotel.kafka.producer;

import com.pser.hotel.domain.hotel.dto.RoomDto;
import com.pser.hotel.global.config.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomStatusProducer {
    private final KafkaTemplate<String, RoomDto> roomDtoValueKafkaTemplate;

    public void onCreated(RoomDto roomDto) {
        roomDtoValueKafkaTemplate.send(KafkaTopics.ROOM_CREATED, roomDto);
    }

    public void onUpdated(RoomDto roomDto) {
        roomDtoValueKafkaTemplate.send(KafkaTopics.ROOM_UPDATED, roomDto);
    }

    public void onDeleted(RoomDto roomDto) {
        roomDtoValueKafkaTemplate.send(KafkaTopics.ROOM_DELETED, roomDto);
    }
}
