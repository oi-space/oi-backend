package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.request.RoomSearchRequest;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
@ActiveProfiles("test")
@DisplayName("RoomDaoImpl 테스트")
class RoomDaoImplTest {
    @Autowired
    RoomDao roomDao;
    User user;
    Hotel hotel;
    List<Room> rooms;
    Random rnd = new Random();
    RoomSearchRequest request;
    Pageable pageable;

    @BeforeEach
    public void setUp() {
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        rooms = Utils.createRooms(hotel, 10);
        roomDao.saveAll(rooms);
    }

    private RoomSearchRequest createRoomSearchRequestByMaxCapacityLte(int maxCapacityLte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .maxCapacityLte(maxCapacityLte)
                .build();
        return request;
    }

    private RoomSearchRequest createRoomSearchRequestByMaxCapacityGte(int maxCapacityGte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .maxCapacityGte(maxCapacityGte)
                .build();
        return request;
    }

    private RoomSearchRequest createRoomSearchRequestByStandartCapacityLte(int standardCapacityLte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .standardCapacityLte(standardCapacityLte)
                .build();
        return request;
    }

    private RoomSearchRequest createRoomSearchRequestByStandartCapacityGte(int standardCapacityLte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .standardCapacityGte(standardCapacityLte)
                .build();
        return request;
    }

    private RoomSearchRequest createRoomSearchRequestByPriceLte(int priceLte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .priceLte(priceLte)
                .build();
        return request;
    }

    private RoomSearchRequest createRoomSearchRequestByPriceGte(int priceGte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .priceGte(priceGte)
                .build();
        return request;
    }

    private RoomSearchRequest createRoomSearchRequestByKeyword(String keyword) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .keyword(keyword)
                .build();
        return request;
    }

    private RoomSearchRequest createRoomSearchRequestByAmenity() {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .tv(rnd.nextBoolean())
                .pet(rnd.nextBoolean())
                .build();
        return request;
    }

    private Pageable createPageable() {
        return PageRequest.of(0, 10);
    }
}