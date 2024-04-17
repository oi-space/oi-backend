package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import java.time.LocalDateTime;
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
@Import({QueryDslConfig.class})
@DisplayName("TimeSale 엔티티 테스트")
@Slf4j
class TimeSaleTest {
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
    @DisplayName("TimeSale 생성 테스트")
    public void testSave() {
        //Given
        TimeSale timeSale = TimeSale.builder()
                .room(room)
                .price(1000)
                .startAt(LocalDateTime.of(2020, 1, 1, 12, 0))
                .endAt(LocalDateTime.of(2020, 1, 10, 12, 0))
                .build();

        //When
        em.persist(timeSale);

        //Then
        Assertions.assertThat(em.find(TimeSale.class, timeSale.getId())).isEqualTo(timeSale);
    }

    @Test
    @DisplayName("TimeSale 생성 에러 테스트 - 시작일시가 종료일시 이후인 경우")
    public void testSaveError() {
        //Given
        TimeSale timeSale = TimeSale.builder()
                .room(room)
                .price(1000)
                .endAt(LocalDateTime.of(2020, 1, 1, 12, 0))
                .startAt(LocalDateTime.of(2020, 1, 10, 12, 0))
                .build();

        //When
        Throwable throwable = Assertions.catchThrowable(() -> {
            em.persist(timeSale);
        });

        //Then
        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}