package com.pser.hotel.domain.hotel.dao;


import static com.pser.hotel.domain.hotel.util.Utils.createAmenity;
import static com.pser.hotel.domain.hotel.util.Utils.createHotel;
import static com.pser.hotel.domain.hotel.util.Utils.createImageUrl;
import static com.pser.hotel.domain.hotel.util.Utils.createRoom;
import static com.pser.hotel.domain.hotel.util.Utils.createRoomImages;
import static com.pser.hotel.domain.hotel.util.Utils.createUser;

import com.pser.hotel.domain.hotel.domain.Amenity;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.RoomImage;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
@DisplayName("RommDao 테스트")
@Slf4j
class RoomDaoTest {
    @Autowired
    RoomDao roomDao;
    @Autowired
    EntityManager em;
    User user;
    Hotel hotel;
    Room room;
    List<RoomImage> roomImages = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        user = createUser();
        hotel = createHotel(user);
        room = createRoom(hotel);
        Amenity amenity = createAmenity(room);
        List<String> imageUrls = List.of(createImageUrl(), createImageUrl(), createImageUrl());
        roomImages = createRoomImages(room, imageUrls);
        roomDao.save(room);
    }

    @AfterEach
    public void clear() {
    }

    @Test
    @DisplayName("save 테스트")
    @Transactional
    public void saveTest() {
        Room saveRoom = roomDao.save(createRoom(hotel));

        Assertions.assertThat(saveRoom).isNotNull();
    }

    @Test
    @DisplayName("findBy 테스트")
    public void findByTest() {
        Room findRoom = roomDao.findById(room.getId()).get();

        log.info("room : {}", findRoom);
        log.info("roomAmenity : {}", findRoom.getAmenity());
        log.info("roomImages : {}", findRoom.getRoomImages());

        Assertions.assertThat(findRoom.getId()).isEqualTo(room.getId());
    }
}