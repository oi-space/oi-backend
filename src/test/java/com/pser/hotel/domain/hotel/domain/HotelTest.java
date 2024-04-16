package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
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
@DisplayName("Hotel 엔티티 테스트")
class HotelTest {
    @Autowired
    TestEntityManager testEntityManager;
    EntityManager em;
    Hotel hotel;
    @BeforeEach
    public void setUp(){
        em = testEntityManager.getEntityManager();
    }

    @AfterEach
    public void clear(){
    }

    @Test
    @DisplayName("Hotel 생성 테스트")
    public void save(){
        hotel = createHotel();
        em.persist(hotel);

        createHotelImage();
        createHotelImage();

        Assertions.assertThat(em.contains(hotel)).isTrue();
        Assertions.assertThat(hotel.getImages().size()).isEqualTo(2);
    }

    private User createUser(){
        return User.builder().email("test_email").password("123").build();
    }

    private HotelImage createHotelImage(){
        return HotelImage.builder()
                .imageUrl(getRandomUUID())
                .hotel(hotel)
                .build();
    }

    private String getRandomUUID(){
        return UUID.randomUUID().toString().substring(1,10);
    }

    private Hotel createHotel(){
        Hotel hotel =Hotel.builder()
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
                .user(createUser())
                .build();
        return hotel;
    }
}