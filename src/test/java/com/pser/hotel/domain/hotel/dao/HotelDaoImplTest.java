package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.request.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.response.HotelResponse;
import com.pser.hotel.domain.hotel.dto.response.HotelSummaryResponse;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
public class HotelDaoImplTest {
    @Autowired
    HotelDao hotelDao;
    @Autowired
    FacilityDao facilityDao;
    @Autowired
    UserDao userDao;
    @Autowired
    RoomDao roomDao;
    @Autowired
    EntityManager entityManager;
    User user;
    Hotel hotel;
    Facility facility;
    Room room;
    Reservation reservation;
    HotelSearchRequest hotelSearchRequest;
    Pageable pageable;

    @BeforeEach
    public void setUp() {
        pageable = createPageable();
        user = Utils.createUser();
        userDao.save(user);
        for (int i = 0; i < 10; i++) {
            hotel = Utils.createHotel(user);
            room = Utils.createRoom(hotel);
            reservation = Utils.createReservation(user, room);
            facility = Utils.createFacility(hotel);

            hotelDao.save(hotel);
            roomDao.save(room);
            facilityDao.save(facility);
        }
    }

    @Test
    @DisplayName("name 검색 테스트")
    public void nameSearchTest() {
        hotelSearchRequest = createSearchRequestByName();
        Slice<HotelSummaryResponse> nameResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(nameResponse.getContent().size()).isGreaterThanOrEqualTo(10);
    }

    private HotelSearchRequest createSearchRequestByName() {
        return HotelSearchRequest.builder()
                .name("업체명")
                .build();
    }

    @Test
    @DisplayName("category 검색 테스트")
    public void categorySearchTest() {
        hotelSearchRequest = createSearchRequestByCategory();
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent().size()).isGreaterThanOrEqualTo(10);
    }

    private HotelSearchRequest createSearchRequestByCategory() {
        return HotelSearchRequest.builder()
                .category(HotelCategoryEnum.HOTEL)
                .build();
    }

    @Test
    @DisplayName("province 검색 테스트")
    public void provinceSearchTest() {
        hotelSearchRequest = creatSearchRequestByProvince();
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent().size()).isGreaterThanOrEqualTo(10);
    }

    private HotelSearchRequest creatSearchRequestByProvince() {
        return HotelSearchRequest.builder()
                .province("서울특별시")
                .build();
    }

    @Test
    @DisplayName("district 검색 테스트")
    public void districtSearchTest() {
        hotelSearchRequest = createSearchRequestByDistrict();
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent().size()).isGreaterThanOrEqualTo(10);
    }

    private HotelSearchRequest createSearchRequestByDistrict() {
        return HotelSearchRequest.builder()
                .district("가산동")
                .build();
    }

    @Test
    @DisplayName("detailedAddress 검색 테스트")
    public void detailedAddressTest() {
        hotelSearchRequest = createSearchRequestByDetailedAddress();
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent().size()).isGreaterThanOrEqualTo(10);
    }

    private HotelSearchRequest createSearchRequestByDetailedAddress() {
        return HotelSearchRequest.builder()
                .detailedAddress("가산디지털로")
                .build();
    }

    @Test
    @DisplayName("숙소 평점 테스트")
    public void gradeTest() {
        double grade = hotelDao.getHotelGrade(hotel.getId());
        Assertions.assertThat(grade).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("숙소 전체 조회 테스트")
    public void fndAllTest() {
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.findAllWithGradeAndPrice(pageable);
        Assertions.assertThat(hotelResponse.getContent().size()).isGreaterThanOrEqualTo(10);
    }

    @Test
    @DisplayName("특정 숙소 조회 테스트")
    public void findOneHotel() {
        HotelResponse hotelResponse = hotelDao.findHotel(hotel.getId());
        Assertions.assertThat(hotelResponse.getProvince()).isEqualTo("서울특별시");
    }

    private Pageable createPageable() {
        return PageRequest.of(0, 20);
    }
}
