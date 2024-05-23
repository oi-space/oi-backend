package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.HotelMapper;
import com.pser.hotel.domain.hotel.dto.HotelMapperImpl;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.HotelSummaryResponse;
import com.pser.hotel.domain.hotel.dto.TimesaleMapper;
import com.pser.hotel.domain.hotel.dto.TimesaleMapperImpl;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
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
    PageRequest pageRequest;
    HotelSearchRequest hotelSearchRequest;

    @BeforeEach
    public void setUp() {
        pageRequest = PageRequest.of(0, 10);
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        room = Utils.createRoom(hotel);
        reservation = Utils.createReservation(user, room);
        facility = Utils.createFacility(hotel);

        userDao.save(user);
        hotelDao.save(hotel);
        roomDao.save(room);
        facilityDao.save(facility);
    }

    @Test
    @DisplayName("name 검색 테스트")
    public void nameSearchTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByName(hotel.getName());
        Slice<HotelSummaryResponse> nameResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(nameResponse.getContent()).isNotEmpty();
    }

    private HotelSearchRequest createSearchRequestByName(String name) {
        return HotelSearchRequest.builder()
                .name(name)
                .build();
    }

    @Test
    @DisplayName("category 검색 테스트")
    public void categorySearchTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByCategory(hotel.getCategory());
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }

    private HotelSearchRequest createSearchRequestByCategory(HotelCategoryEnum hotelCategoryEnum) {
        return HotelSearchRequest.builder()
                .category(hotelCategoryEnum)
                .build();
    }

    @Test
    @DisplayName("province 검색 테스트")
    public void provinceSearchTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = creatSearchRequestByProvince(hotel.getProvince());
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }

    private HotelSearchRequest creatSearchRequestByProvince(String province) {
        return HotelSearchRequest.builder()
                .province(province)
                .build();
    }

    @Test
    @DisplayName("district 검색 테스트")
    public void districtSearchTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByDistrict(hotel.getDistrict());
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }

    private HotelSearchRequest createSearchRequestByDistrict(String district) {
        return HotelSearchRequest.builder()
                .district(district)
                .build();
    }

    @Test
    @DisplayName("detailedAddress 검색 테스트")
    public void detailedAddressTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByDetailedAddress(hotel.getDetailedAddress());
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }

    private HotelSearchRequest createSearchRequestByDetailedAddress(String detailedAddress) {
        return HotelSearchRequest.builder()
                .detailedAddress(detailedAddress)
                .build();
    }

    @Test
    @DisplayName("people 검색 테스트")
    public void peopleTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByPeople(room.getMaxCapacity());
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }

    private HotelSearchRequest createSearchRequestByPeople(int maxCapacity) {
        return HotelSearchRequest.builder()
                .people(maxCapacity - 1)
                .build();
    }

    @Test
    @DisplayName("startAt, endAt 검색 테스트")
    public void dateTest() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequestByDate();
        Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
        Assertions.assertThat(hotelResponse.getContent()).isNotEmpty();
    }

    private HotelSearchRequest createSearchRequestByDate() {
        return HotelSearchRequest.builder()
                .searchStartAt(LocalDate.of(2024,4,18))
                .searchEndAt(LocalDate.of(2024,4,19))
                .build();
    }

    @Test
    @DisplayName("숙소 평점 테스트")
    public void gradeTest() {
        double grade = hotelDao.getHotelGrade(hotel.getId());
        Assertions.assertThat(grade).isGreaterThan(-1);
    }

    @Test
    @DisplayName("숙소 전체 조회 테스트")
    public void fndAllTest() {
        Pageable pageable = createPageable();
        Slice<HotelResponse> hotelResponse = hotelDao.findAllWithGradeAndPrice(pageable);
        Assertions.assertThat(hotelResponse).isNotEmpty();
    }

    @Test
    @DisplayName("특정 숙소 조회 테스트")
    public void findOneHotel() {
        HotelResponse hotelResponse = hotelDao.findHotel(hotel.getId());
        Assertions.assertThat(hotelResponse).isNotNull();
    }

    private Pageable createPageable() {
        return PageRequest.of(0, 10);
    }
}
