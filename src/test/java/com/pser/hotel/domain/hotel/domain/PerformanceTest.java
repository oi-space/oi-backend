package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.dao.FacilityDao;
import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.dao.TimesaleDao;
import com.pser.hotel.domain.hotel.dto.request.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.response.HotelSummaryResponse;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
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
@DisplayName("Hotel 엔티티 인덱스 성능 테스트")
public class PerformanceTest {
    @Autowired
    HotelDao hotelDao;
    @Autowired
    TimesaleDao timesaleDao;
    @Autowired
    RoomDao roomDao;
    @Autowired
    FacilityDao facilityDao;
    @Autowired
    EntityManager entityManager;
    User user;
    Hotel hotel;
    Facility facility;
    Room room;
    TimeSale timeSale;
    int performanceSize = 100000;
    HotelSearchRequest hotelSearchRequest;
    @BeforeEach
    public void setUp() {
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        facility = Utils.createFacility(hotel);

        hotelDao.save(hotel);
    }

    @Test
    @DisplayName("검색 성능 테스트")
    public void queryPerformanceAboutSearchIndex() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequest();
        double sum = 0;
        for(int i=0; i < 10; i++){
            long startTime = System.currentTimeMillis();
            Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
            System.out.println("검색 결과 : " + hotelResponse.getContent());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            sum+=duration;
        }

        System.out.println("실행 시간 평균 : " + sum/10 + "ms");

        Assertions.assertThat(sum).isLessThan(10000); // 10초 이내에 쿼리가 실행되어야 함
    }

    @Test
    @DisplayName("타임특가 적용 숙소 전체 조회 성능 테스트")
    public void queryPerformanceAboutFindNowTimesaleHotelIndex() {
        Pageable pageable = createPageable();
        double sum = 0;
        for(int i=0; i < 1; i++) {
            long startTime = System.currentTimeMillis();
            Slice<HotelSummaryResponse> timesaleResponse = timesaleDao.findNowTimesaleHotel(pageable);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            sum+=duration;
        }

        System.out.println("실행 시간 평균 : " + sum/10 + "ms");

        Assertions.assertThat(sum).isLessThan(100000); // 100초 이내에 쿼리가 실행되어야 함
    }

    private HotelSearchRequest createSearchRequest() {
        return  HotelSearchRequest.builder()
                .name("업체명" + (performanceSize - (int) performanceSize / 10))
                .city("금천구" + (performanceSize - (int) performanceSize / 10))
                .province("서울특별시" + (performanceSize - (int) performanceSize / 10))
                .build();
    }

    private Hotel createHotel(User user, int number) {
        HotelCategoryEnum hotelCategoryEnum = HotelCategoryEnum.HOTEL;
        if(number == performanceSize - (int) performanceSize / 10) {
            hotelCategoryEnum = HotelCategoryEnum.PENSION;
        }
        Hotel hotel1 = Hotel.builder()
                .name("업체명" + number)
                .category(hotelCategoryEnum)
                .description("업체설명")
                .notice("업체공자")
                .province("서울특별시" + number)
                .city("금천구" + number)
                .district("가산동")
                .detailedAddress("가산디지털로1로 189")
                .latitude(100.123)
                .longitude(123.100)
                .mainImage("mainImg.url")
                .businessNumber("123456-123456")
                .certUrl("cert.url")
                .visitGuidance("가산디지털단지역 도보 5분")
                .user(user)
                .build();
        return hotel1;
    }

    private Room createRoom(Hotel hotel, int number) {
        return Room.builder()
                .hotel(hotel)
                .name("객실 이름" + number)
                .description("객실 설명" + number)
                .precaution("객실 설명" + number)
                .totalRooms(new Random().nextInt(1, 100))
                .maxCapacity(new Random().nextInt(11, 20))
                .standardCapacity(new Random().nextInt(1, 10))
                .price(100000 + number)
                .checkIn(LocalTime.of(15, 00, 0))
                .checkOut(LocalTime.of(11, 00, 00))
                .build();

    }

    public static TimeSale createTimesale(Room room, int number) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startAt = now.minusDays(1);
        LocalDateTime endAt = now.plusDays(1);

        return TimeSale.builder()
                .room(room)
                .price(70000 + number)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }

    private Pageable createPageable() {
        return PageRequest.of(0, 10);
    }
}
