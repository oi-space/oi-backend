package com.pser.hotel.domain.hotel.dao;


import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import com.pser.hotel.domain.hotel.dto.TimesaleHotelResponse;
import com.pser.hotel.domain.hotel.dto.TimesaleMapper;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
public class TimesaleDaoImplTest {
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
    @DisplayName("room id로 숙소 조회 기능 테스트")
    public void findHotelByRoomIdTest() {
        Hotel optionalHotel = timesaleDao.findHotelByRoomId(room.getId()).orElseThrow();
        Assertions.assertThat(optionalHotel).isEqualTo(hotel);
    }

    @Test
    @DisplayName("timesale id로 숙소 조회 기능 테스트")
    public void findHotelByTimesaleIdTest() {
        Hotel optionalHotel = timesaleDao.findHotelByTimesaleId(timeSale.getId()).orElseThrow();
        Assertions.assertThat(optionalHotel).isEqualTo(hotel);
    }

    @Test
    @DisplayName("타임특가 적용 숙소 전체 조회 기능 테스트")
    public void findNowTimesaleHotel() {
        Slice<TimesaleHotelResponse> timesaleHotelList = timesaleDao.findNowTimesaleHotel(pageRequest);
        boolean isHotelExist = false;
        for (TimesaleHotelResponse response : timesaleHotelList) {
            if (response.getName().equals(hotel.getName())) {
                isHotelExist = true;
                break;
            }
        }
        Assertions.assertThat(isHotelExist).isTrue();
    }
}
