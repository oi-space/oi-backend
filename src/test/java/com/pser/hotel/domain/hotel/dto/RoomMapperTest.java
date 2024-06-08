package com.pser.hotel.domain.hotel.dto;

import static com.pser.hotel.domain.hotel.util.Utils.createHotel;
import static com.pser.hotel.domain.hotel.util.Utils.createRoom;
import static com.pser.hotel.domain.hotel.util.Utils.createUser;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.mapper.RoomMapper;
import com.pser.hotel.domain.hotel.dto.mapper.RoomMapperImpl;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import com.pser.hotel.domain.hotel.dto.response.RoomDetailResponse;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

@Import({QueryDslConfig.class, MapperConfig.class})
@DisplayName("RoomMapper 테스트")
@Slf4j
class RoomMapperTest {

    RoomMapper roomMapper;
    Random rnd = new Random();
    User user;
    Hotel hotel;

    @BeforeEach
    public void setUp() {
        roomMapper = new RoomMapperImpl();
        user = createUser();
        hotel = createHotel(user);
    }

    @Test
    @DisplayName("Room -> RoomDetailResponse 테스트")
    void test() {
        // Given
        Room room = createRoom(hotel);
        Utils.createAmenity(room);

        // When
        RoomDetailResponse response = roomMapper.roomToRoomDetailResponse(room);

        // then
        Assertions.assertThat(room.getAmenity().getHeatingSystem()).isEqualTo(response.getHeatingSystem());
        Assertions.assertThat(room.getAmenity().getTv()).isEqualTo(response.getTv());
        Assertions.assertThat(room.getAmenity().getRefrigerator()).isEqualTo(response.getRefrigerator());
        Assertions.assertThat(room.getAmenity().getAirConditioner()).isEqualTo(response.getAirConditioner());
        Assertions.assertThat(room.getAmenity().getWasher()).isEqualTo(response.getWasher());
        Assertions.assertThat(room.getAmenity().getTerrace()).isEqualTo(response.getTerrace());
        Assertions.assertThat(room.getAmenity().getCoffeeMachine()).isEqualTo(response.getCoffeeMachine());
        Assertions.assertThat(room.getAmenity().getInternet()).isEqualTo(response.getInternet());
        Assertions.assertThat(room.getAmenity().getKitchen()).isEqualTo(response.getKitchen());
        Assertions.assertThat(room.getAmenity().getBathtub()).isEqualTo(response.getBathtub());
        Assertions.assertThat(room.getAmenity().getIron()).isEqualTo(response.getIron());
        Assertions.assertThat(room.getAmenity().getPool()).isEqualTo(response.getPool());
        Assertions.assertThat(room.getAmenity().getPet()).isEqualTo(response.getPet());
    }


    @Test
    @DisplayName("RoomRequest -> Room 테스트")
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

    private RoomRequest createRoomRequestDto() {
        return new RoomRequest(
                "객실이름", "설명", "mainImage.url", "주의사항", 1000, LocalTime.of(15, 00), LocalTime.of(11, 00),
                1, 1, 1,
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), List.of()
        );
    }
}