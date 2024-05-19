package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.member.domain.Profile;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
@DisplayName("Facility 엔티티 테스트")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class FacilityTest {
    @Autowired
    TestEntityManager testEntityManager;
    EntityManager em;
    Facility facility;
    Hotel hotel;
    User user;

    @BeforeEach
    public void setUp() {
        em = testEntityManager.getEntityManager();
        user = createUser();
        hotel = createHotel();
        facility = createFacility();
        em.persist(createProfile(user));
        em.persist(hotel);
    }

    @AfterEach
    public void clear() {
    }

    @Test
    @DisplayName("Facility 생성 테스트")
    public void save() {
        Assertions.assertThat(em.contains(facility)).isTrue();
    }

    private Profile createProfile(User user) {
        return Profile.builder()
                .user(user)
                .username("test_username")
                .phone("010-111-222")
                .imageUrl("testImageUrl")
                .description("test_description")
                .build();
    }

    private Facility createFacility() {
        return facility = Facility.builder()
                .parkingLot(true)
                .barbecue(true)
                .wifi(true)
                .sauna(true)
                .swimmingPool(true)
                .restaurant(true)
                .roofTop(true)
                .fitness(true)
                .dryer(true)
                .breakfast(true)
                .smokingArea(true)
                .allTimeDesk(true)
                .luggageStorage(true)
                .snackBar(true)
                .petFriendly(true)
                .hotel(hotel)
                .build();
    }

    private Hotel createHotel() {
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
        return hotel;
    }

    private User createUser() {
        return User.builder().email("test_email").password("123").build();
    }

}