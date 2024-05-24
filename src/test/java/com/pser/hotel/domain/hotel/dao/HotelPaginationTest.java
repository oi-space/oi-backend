package com.pser.hotel.domain.hotel.dao;

import static com.pser.hotel.domain.hotel.domain.QHotel.hotel;
import static com.pser.hotel.domain.hotel.util.Utils.createUser;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
@DisplayName("Hotel 페이지네이션 테스트")
@Slf4j
public class HotelPaginationTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    HotelDao hotelDao;

    @Autowired
    EntityManager em;

    Random rnd = new Random();

    List<Hotel> hotels = new ArrayList<>();

    List<String> locations = List.of("서울", "경기", "인천", "강원", "충북", "충남", "제주");

    Map<String, Integer> locationMap = new LinkedHashMap<String, Integer>() {{
        put("서울", 0);
        put("경기", 0);
        put("인천", 0);
        put("강원", 0);
        put("충북", 0);
        put("충남", 0);
        put("제주", 0);
    }};

    @BeforeEach
    public void setUp() {
        // @Transactional 어노테이션은 데이터 정합성을 위해 AUTO_INCREMENT를 롤백해주지 않아서 nativeQuery를 작성했습니다.
        this.em.createNativeQuery("ALTER TABLE user AUTO_INCREMENT = 1").executeUpdate();
        this.em.createNativeQuery("ALTER TABLE hotel AUTO_INCREMENT = 1").executeUpdate();
        this.em.createNativeQuery("ALTER TABLE room AUTO_INCREMENT = 1").executeUpdate();
        this.em.createNativeQuery("ALTER TABLE reservation AUTO_INCREMENT = 1").executeUpdate();
    }

    @Test
    @DisplayName("오프셋 기반 페이지네이션 테스트")
    public void offsetPaginationTest() {
        // Given
        hotelDao.saveAll(createHotels(10));
        Pageable beforePageable = createPageable(0, 5);
        Pageable afterPageable = createPageable(1, 5);

        List<Long> beforeIds = queryFactory.select(hotel.id)
                .from(hotel)
                .offset(beforePageable.getOffset())
                .limit(beforePageable.getPageSize())
                .orderBy(hotel.createdAt.desc())
                .fetch();

        hotelDao.saveAll(createHotels(5));

        // When
        List<Long> afterIds = queryFactory.select(hotel.id)
                .from(hotel)
                .offset(afterPageable.getOffset())
                .limit(afterPageable.getPageSize())
                .orderBy(hotel.createdAt.desc())
                .fetch();

        // Then
        beforeIds.stream().forEach(e ->
                Assertions.assertThat(afterIds.contains(e)).isTrue());
    }

    @Test
    @DisplayName("커서 기반 페이지네이션 테스트")
    public void cursorPaginationTest() {
        // Given
        hotelDao.saveAll(createHotels(10));
        Pageable beforePageable = createPageable(0, 5);
        Pageable afterPageable = createPageable(1, 5);

        List<Long> beforeIds = queryFactory.select(hotel.id)
                .from(hotel)
                .limit(beforePageable.getPageSize())
                .orderBy(hotel.createdAt.desc())
                .fetch();

        hotelDao.saveAll(createHotels(5));

        // When
        Long cursorId = beforeIds.get(beforeIds.size() - 1);
        List<Long> afterIds = queryFactory.select(hotel.id)
                .from(hotel)
                .where(hotel.id.lt(cursorId))
                .limit(afterPageable.getPageSize())
                .orderBy(hotel.createdAt.desc())
                .fetch();

        // Then
        beforeIds.stream().forEach(e ->
                Assertions.assertThat(afterIds.contains(e)).isFalse());
    }

    public List<Hotel> createHotels(int idx) {
        List<Hotel> saveHotels = IntStream.rangeClosed(1, idx)
                .mapToObj(e -> {
                    User user = createUser();
                    String province = locations.get(rnd.nextInt(0, locations.size()));
                    return createHotel(province, randomUUID(), randomUUID(), user);
                }).toList();
        hotels.addAll(saveHotels);
        return saveHotels;
    }

    public Hotel createHotel(String province, String city, String district, User user) {
        return Hotel.builder()
                .name(String.format("업체명_%s", randomUUID()))
                .category(HotelCategoryEnum.HOTEL)
                .description(String.format("업체설명_%s", randomUUID()))
                .notice(String.format("업체공자_%s", randomUUID()))
                .province(province)
                .city(city)
                .district(district)
                .detailedAddress("가산디지털로1로 189")
                .latitude(100.123)
                .longtitude(123.100)
                .mainImage("mainImg.url")
                .businessNumber("123456-123456")
                .certUrl("cert.url")
                .visitGuidance("가산디지털단지역 도보 5분")
                .user(user)
                .build();
    }

    private Pageable createPageable(int offset, int size) {
        return PageRequest.of(offset, size);
    }

    public String randomUUID() {
        return UUID.randomUUID().toString().substring(1, 11);
    }
}
