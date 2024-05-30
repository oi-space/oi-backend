package com.pser.hotel.domain.hotel.dto;

import static com.pser.hotel.domain.hotel.util.Utils.createHotel;
import static com.pser.hotel.domain.hotel.util.Utils.createRoom;
import static com.pser.hotel.domain.hotel.util.Utils.createUser;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.mapper.RoomMapper;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import com.pser.hotel.domain.member.domain.User;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("RoomMapper 테스트")
@Slf4j
class RoomMapperTest {
    @Autowired
    RoomMapper roomMapper;
    Random rnd = new Random();
    User user;
    Hotel hotel;

    @BeforeEach
    public void setUp() {
        user = createUser();
        hotel = createHotel(user);
    }


    @Test
    @DisplayName("RoomMapper 업데이트 테스트")
    void updateRoomFromDto() {
        // Given
        Room room = createRoom(hotel);
        RoomRequest request = createRoomRequestDto();
        log.info("room Naem : {}", room.getName());
        log.info("roomRequest Naem : {}", request.getName());

        // When
        roomMapper.updateRoomFromDto(request, room);

        // Then
        Assertions.assertThat(room.getName()).isEqualTo(request.getName());
        Assertions.assertThat(room.getDescription()).isEqualTo(request.getDescription());
        Assertions.assertThat(room.getPrecaution()).isEqualTo(request.getPrecaution());
        Assertions.assertThat(room.getPrice()).isEqualTo(request.getPrice());
        Assertions.assertThat(room.getCheckIn()).isEqualTo(request.getCheckIn());
        Assertions.assertThat(room.getCheckOut()).isEqualTo(request.getCheckOut());
        Assertions.assertThat(room.getStandardCapacity()).isEqualTo(request.getStandardCapacity());
        Assertions.assertThat(room.getMaxCapacity()).isEqualTo(request.getMaxCapacity());
        Assertions.assertThat(room.getTotalRooms()).isEqualTo(request.getTotalRooms());
    }

    @Test
    @DisplayName("RoomMapper 업데이트 테스트")
    void updateRoomFromDtoByNull() {
        // Given
        Room room = createRoom(hotel);
        RoomRequest request = new RoomRequest();
        log.info("room Name : {}", room.getName());
        log.info("roomRequest Name : {}", request.getName());

        // When
        roomMapper.updateRoomFromDto(request, room);

        // Then
        Assertions.assertThat(room.getName()).isNotNull();
        Assertions.assertThat(room.getDescription()).isNotNull();
        Assertions.assertThat(room.getPrice()).isNotNull();
        Assertions.assertThat(room.getPrecaution()).isNotNull();
        Assertions.assertThat(room.getCheckIn()).isNotNull();
        Assertions.assertThat(room.getCheckOut()).isNotNull();
        Assertions.assertThat(room.getStandardCapacity()).isNotNull();
        Assertions.assertThat(room.getMaxCapacity()).isNotNull();
        Assertions.assertThat(room.getTotalRooms()).isNotNull();
    }

    private RoomRequest createRoomRequestDto() {
        return new RoomRequest(
                1L,
                "객실이름", "설명", "주의사항", 1000, LocalTime.of(15, 00), LocalTime.of(11, 00),
                1, 1, 1,
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), List.of()
        );
    }
}