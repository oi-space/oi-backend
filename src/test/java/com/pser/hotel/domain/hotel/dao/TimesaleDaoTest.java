package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import com.pser.hotel.domain.hotel.dto.TimesaleMapper;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
public class TimesaleDaoTest {
    @Autowired
    TimesaleDao timesaleDao;
    @Autowired
    HotelDao hotelDao;
    @Autowired
    UserDao userDao;
    @Autowired
    RoomDao roomDao;
    @Autowired
    TimesaleMapper timesaleMapper;
    @Autowired
    EntityManager entityManager;
    PageRequest pageRequest;
    User user;
    Hotel hotel;
    Room room;
    TimeSale timeSale;
    Reservation reservation;

    @BeforeEach
    public void setUp() {
        pageRequest = PageRequest.of(0, 10);
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        room = Utils.createRoom(hotel);
        reservation = Utils.createReservation(user, room);
        timeSale = Utils.createTimesale(room);

        userDao.save(user);
        hotelDao.save(hotel);
        roomDao.save(room);
        timesaleDao.save(timeSale);
    }

    @Test
    @DisplayName("room id를 통해 timesale 정보 조회 테스트")
    public void findByRoomIdTest() {
        List<TimeSale> timeSaleList = timesaleDao.findByRoomId(room.getId());
        boolean isTimesaleExist = false;
        for(TimeSale ele : timeSaleList) {
            if(ele.getId().equals(timeSale.getId())){
                isTimesaleExist = true;
                break;
            }
        }
        Assertions.assertThat(isTimesaleExist).isTrue();
    }
}
