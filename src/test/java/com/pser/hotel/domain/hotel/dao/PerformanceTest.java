package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.dto.request.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.request.RoomSearchRequest;
import com.pser.hotel.domain.hotel.dto.response.HotelSummaryResponse;
import com.pser.hotel.domain.hotel.dto.response.RoomResponse;
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
    int performanceSize = 100000;
    HotelSearchRequest hotelSearchRequest;
    @BeforeEach
    public void setUp() {
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        facility = Utils.createFacility(hotel);
    }

    @Test
    @DisplayName("검색 등록 성능 테스트")
    public void queryPerformanceAboutIndexPost() {
        long startTime = System.currentTimeMillis();
        for(int i=0; i < 1000; i++){
            hotel = Utils.createHotel(user);
            facility = Utils.createFacility(hotel);

            hotelDao.save(hotel);
            facilityDao.save(facility);
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("실행 시간 평균 : " + (double)duration/1000 + "ms");
        Assertions.assertThat(duration).isLessThan(10000); // 10초 이내에 쿼리가 실행되어야 함
    }

    @Test
    @DisplayName("검색 조회 성능 테스트")
    public void queryPerformanceAboutSearchIndexGet() {
        Pageable pageable = createPageable();
        hotelSearchRequest = createSearchRequest();
        long startTime = System.currentTimeMillis();
        for(int i=0; i < 10; i++){
            Slice<HotelSummaryResponse> hotelResponse = hotelDao.search(hotelSearchRequest, pageable);
            System.out.println("검색 결과 : " + hotelResponse.getContent());
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("실행 시간 평균 : " + duration/10 + "ms");

        Assertions.assertThat(duration).isLessThan(10000); // 10초 이내에 쿼리가 실행되어야 함
    }

    @Test
    @DisplayName("타임특가 적용 숙소 전체 조회 성능 테스트")
    public void queryPerformanceAboutFindNowTimesaleHotelIndex() {
        Pageable pageable = createPageable();
        long startTime = System.currentTimeMillis();
        Slice<HotelSummaryResponse> timesaleResponse = timesaleDao.findNowTimesaleHotel(pageable);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("실행 시간 : " + duration + "ms");

        Assertions.assertThat(duration).isLessThan(100000); // 100초 이내에 쿼리가 실행되어야 함
    }

    @Test
    @DisplayName("객실 검색 조회 성능 테스트")
    public void queryPerformanceAboutRoomSearchIndex() {
        Pageable pageable = createPageable();
        RoomSearchRequest roomSearchRequest = createRoomSearchRequest();
        long startTime = System.currentTimeMillis();
        Slice<RoomResponse> roomResponses = roomDao.search(roomSearchRequest, pageable);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("실행 시간 평균 : " + duration + "ms");

        Assertions.assertThat(duration).isLessThan(100000); // 100초 이내에 쿼리가 실행되어야 함
    }

    @Test
    @DisplayName("숙소 전체 조회 성능 테스트")
    public void queryPerformanceAboutFindAllHotelIndex() {
        Pageable pageable = createPageable();
        long startTime = System.currentTimeMillis();
        Slice<HotelSummaryResponse> hotelSummaryResponses = hotelDao.findAllWithGradeAndPrice(pageable);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("실행 시간 평균 : " + duration + "ms");

        Assertions.assertThat(duration).isLessThan(100000); // 100초 이내에 쿼리가 실행되어야 함
    }

    private HotelSearchRequest createSearchRequest() {
        return  HotelSearchRequest.builder()
                .name("업체명" + (performanceSize - performanceSize / 10))
                .city("금천구" + (performanceSize - performanceSize / 10))
                .province("서울특별시" + (performanceSize - performanceSize / 10))
                .build();
    }

    private RoomSearchRequest createRoomSearchRequest() {
        return RoomSearchRequest.builder()
                .price(109000)
                .build();
    }

    private Pageable createPageable() {
        return PageRequest.of(0, 10);
    }
}
