package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
@DisplayName("Amenity 엔티티 테스트")
@Slf4j
class AmenityTest {
    @Autowired
    TestEntityManager em;
    Room room;

    @BeforeEach
    public void setUp() {
        User user = User.builder()
                .email("email@gmail.com")
                .password("password")
                .build();
        Hotel hotel = Hotel.builder()
                .name("업체명")
                .category(HotelCategoryEnum.HOTEL)
                .description("설명")
                .notice("공지")
                .province("서울특별시")
                .city("금천구")
                .district("가산동")
                .detailedAddress("가산디지털로1로 189")
                .latitude(100.123)
                .longtitude(123.100)
                .mainImage("mainImg.url")
                .businessNumber("123456-123456")
                .certUrl("cert.url")
                .visitGuidance("가산디지털단지역 도보 5분")
                .user(user)
                .build();
        room = Room.builder()
                .hotel(hotel)
                .name("name")
                .price(1000)
                .checkIn(LocalTime.of(17, 0))
                .checkOut(LocalTime.of(12, 0))
                .totalRooms(10)
                .standardCapacity(2)
                .maxCapacity(5)
                .precaution("주의사항")
                .description("설명")
                .build();
        em.persist(room);
    }

    @Test
    @DisplayName("Amenity 생성 테스트")
    public void testSave() {
        //Given
        Amenity amenity = Amenity.builder()
                .room(room)
                .heatingSystem(true)
                .tv(true)
                .refrigerator(true)
                .airConditioner(true)
                .washer(true)
                .terrace(false)
                .coffeeMachine(false)
                .internet(false)
                .kitchen(false)
                .bathtub(false)
                .iron(false)
                .pool(false)
                .pet(false)
                .inAnnex(false)
                .build();

        //When
        em.persist(amenity);

        //Then
        Amenity amenityFromDb = em.find(Amenity.class, amenity.getId());
        Assertions.assertThat(amenityFromDb.getHeatingSystem()).isEqualTo(amenity.getHeatingSystem());
        Assertions.assertThat(amenityFromDb.getTv()).isEqualTo(amenity.getTv());
        Assertions.assertThat(amenityFromDb.getRefrigerator()).isEqualTo(amenity.getRefrigerator());
        Assertions.assertThat(amenityFromDb.getAirConditioner()).isEqualTo(amenity.getAirConditioner());
        Assertions.assertThat(amenityFromDb.getWasher()).isEqualTo(amenity.getWasher());
        Assertions.assertThat(amenityFromDb.getTerrace()).isEqualTo(amenity.getTerrace());
        Assertions.assertThat(amenityFromDb.getCoffeeMachine()).isEqualTo(amenity.getCoffeeMachine());
        Assertions.assertThat(amenityFromDb.getInternet()).isEqualTo(amenity.getInternet());
        Assertions.assertThat(amenityFromDb.getKitchen()).isEqualTo(amenity.getKitchen());
        Assertions.assertThat(amenityFromDb.getBathtub()).isEqualTo(amenity.getBathtub());
        Assertions.assertThat(amenityFromDb.getIron()).isEqualTo(amenity.getIron());
        Assertions.assertThat(amenityFromDb.getPool()).isEqualTo(amenity.getPool());
        Assertions.assertThat(amenityFromDb.getPet()).isEqualTo(amenity.getPet());
        Assertions.assertThat(amenityFromDb.getInAnnex()).isEqualTo(amenity.getInAnnex());
    }
}