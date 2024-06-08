package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
public class FacilityDaoTest {
    @Autowired
    EntityManager entityManager;
    User user;
    Hotel hotel;
    Facility facility;
    @Autowired
    private FacilityDao facilityDao;
    @Autowired
    private HotelDao hotelDao;

    @BeforeEach
    public void setUp() {
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        facility = Utils.createFacility(hotel);
        hotelDao.save(hotel);
        facilityDao.save(facility);
    }

    @Test
    @DisplayName("save 테스트")
    public void saveFacility() {
        Facility saveFacility = facilityDao.save(facility);
        Assertions.assertThat(saveFacility).isNotNull();
    }

    @Test
    @DisplayName("findByHotelId 테스트")
    public void findByHotelIdTest() {
        Optional<Facility> findFacility = facilityDao.findByHotelId(hotel.getId());
        Assertions.assertThat(findFacility).isNotNull();
    }

    @Test
    @DisplayName("delete 테스트")
    public void deleteByHotelId() {
        facilityDao.deleteByHotelId(hotel.getId());
    }
}
