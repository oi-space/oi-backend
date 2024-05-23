package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.dto.HotelMapper;
import com.pser.hotel.domain.hotel.dto.HotelMapperImpl;
import com.pser.hotel.domain.hotel.dto.TimesaleMapper;
import com.pser.hotel.domain.hotel.dto.TimesaleMapperImpl;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
public class HotelDaoTest {
    @Autowired
    HotelDao hotelDao;
    @Autowired
    EntityManager entityManager;
    User user;
    Hotel hotel;
    Facility facility;

    @BeforeEach
    public void setUp() {
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        facility = Utils.createFacility(hotel);
        hotelDao.save(hotel);
    }

    @Test
    @DisplayName("save 테스트")
    public void saveHotel() {
        Hotel saveHotel = hotelDao.save(hotel);
        Assertions.assertThat(saveHotel).isNotNull();
    }

    @Test
    @DisplayName("findById 테스트")
    public void findByIdTest() {
        Hotel findHotel = hotelDao.findById(hotel.getId()).get();
        Assertions.assertThat(findHotel.getId()).isEqualTo(hotel.getId());
    }
}
