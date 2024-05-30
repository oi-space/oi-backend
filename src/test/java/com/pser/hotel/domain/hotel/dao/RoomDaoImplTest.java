package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.response.RoomResponse;
import com.pser.hotel.domain.hotel.dto.request.RoomSearchRequest;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import java.util.List;
import java.util.Random;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
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

    @Test
    public void searchByMaxCapacity_Lte() {
        request = createRoomSearchRequestByMaxCapacityLte(15);
        pageable = createPageable();

        Page<RoomResponse> fetch = roomDao.search(request, pageable);
        List<Room> expect = rooms.stream().filter(e -> e.getMaxCapacity() <= request.getMaxCapacityLte())
                .toList();
        Assertions.assertThat(fetch.getContent().size()).isEqualTo(expect.size());
    }

    private RoomSearchRequest createRoomSearchRequestByMaxCapacityLte(int maxCapacityLte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .maxCapacityLte(maxCapacityLte)
                .build();
        return request;
    }

    @Test
    public void searchByMaxCapacity_Gte() {
        request = createRoomSearchRequestByMaxCapacityGte(15);
        pageable = createPageable();

        Page<RoomResponse> fetch = roomDao.search(request, pageable);
        List<Room> expect = rooms.stream().filter(e -> e.getMaxCapacity() >= request.getMaxCapacityGte())
                .toList();
        Assertions.assertThat(fetch.getContent().size()).isEqualTo(expect.size());
    }

    private RoomSearchRequest createRoomSearchRequestByMaxCapacityGte(int maxCapacityGte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .maxCapacityGte(maxCapacityGte)
                .build();
        return request;
    }

    @Test
    public void searchByStandardCapacity_Lte() {
        request = createRoomSearchRequestByStandartCapacityLte(5);
        pageable = createPageable();

        Page<RoomResponse> fetch = roomDao.search(request, pageable);
        List<Room> expect = rooms.stream().filter(e -> e.getStandardCapacity() <= request.getStandardCapacityLte())
                .toList();
        Assertions.assertThat(fetch.getContent().size()).isEqualTo(expect.size());
    }

    private RoomSearchRequest createRoomSearchRequestByStandartCapacityLte(int standardCapacityLte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .standardCapacityLte(standardCapacityLte)
                .build();
        return request;
    }

    @Test
    public void searchByStandardCapacity_Gte() {
        request = createRoomSearchRequestByStandartCapacityGte(5);
        pageable = createPageable();

        Page<RoomResponse> fetch = roomDao.search(request, pageable);
        List<Room> expect = rooms.stream().filter(e -> e.getStandardCapacity() >= request.getStandardCapacityGte())
                .toList();
        Assertions.assertThat(fetch.getContent().size()).isEqualTo(expect.size());
    }

    private RoomSearchRequest createRoomSearchRequestByStandartCapacityGte(int standardCapacityLte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .standardCapacityGte(standardCapacityLte)
                .build();
        return request;
    }

    @Test
    public void searchByPrice_Lte() {
        request = createRoomSearchRequestByPriceLte(1000);
        pageable = createPageable();

        Page<RoomResponse> fetch = roomDao.search(request, pageable);
        List<Room> expect = rooms.stream().filter(e -> e.getPrice() < request.getPriceLte()).toList();
        Assertions.assertThat(fetch.getContent().size()).isEqualTo(expect.size());
    }

    private RoomSearchRequest createRoomSearchRequestByPriceLte(int priceLte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .priceLte(priceLte)
                .build();
        return request;
    }

    @Test
    public void searchByPrice_Gte() {
        request = createRoomSearchRequestByPriceGte(1000);
        pageable = createPageable();

        Page<RoomResponse> fetch = roomDao.search(request, pageable);
        List<Room> expect = rooms.stream().filter(e -> e.getPrice() > request.getPriceGte()).toList();
        Assertions.assertThat(fetch.getContent().size()).isEqualTo(expect.size());
    }

    private RoomSearchRequest createRoomSearchRequestByPriceGte(int priceGte) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .priceGte(priceGte)
                .build();
        return request;
    }

    @Test
    public void searchByName() {
        String keyword = "3";
        request = createRoomSearchRequestByKeyword(keyword);
        pageable = createPageable();

        Page<RoomResponse> fetch = roomDao.search(request, pageable);
        List<Room> expect = rooms.stream().filter(e ->
                e.getName().contains(keyword) || e.getDescription().contains(keyword) || e.getPrecaution()
                        .contains(keyword)).toList();
        Assertions.assertThat(fetch.getContent().size()).isEqualTo(expect.size());
    }

    private RoomSearchRequest createRoomSearchRequestByKeyword(String keyword) {
        RoomSearchRequest request = RoomSearchRequest.builder()
                .keyword(keyword)
                .build();
        return request;
    }

    @Test
    public void searchByAmenity() {
        request = createRoomSearchRequestByAmenity();
        pageable = createPageable();

        Page<RoomResponse> fetch = roomDao.search(request, pageable);
        List<Room> expect = rooms.stream().filter(e ->
                e.getAmenity().getTv() == true && e.getAmenity().getPet() == true).toList();
        Assertions.assertThat(fetch.getContent().size()).isEqualTo(expect.size());
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