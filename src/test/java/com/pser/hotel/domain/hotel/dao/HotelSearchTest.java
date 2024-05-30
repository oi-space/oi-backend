package com.pser.hotel.domain.hotel.dao;

import static com.pser.hotel.domain.hotel.domain.QHotel.hotel;
import static com.pser.hotel.domain.hotel.domain.QRoom.room;
import static com.pser.hotel.domain.hotel.util.Utils.createAmenity;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.request.HotelSearchRequest;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;
import java.util.stream.Stream;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
@Transactional
@Slf4j
public class HotelSearchTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    RoomDao roomDao;

    @Autowired
    EntityManager em;

    @Autowired
    PlatformTransactionManager transactionManager;

    Random rnd = new Random();

    Integer confirmMaxCapacity = 0;

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
        em.createQuery("DELETE FROM Room").executeUpdate();
    }

    @Test
    @DisplayName("여행지 (province) 키워드에 따른 검색 테스트")
    public void searchByLocation() {
        // Given
        createTestData(rnd.nextInt(10));
        String keyword = locations.get(rnd.nextInt(0, locations.size()));
        HotelSearchRequest request = HotelSearchRequest.builder()
                .province(keyword).build();

        // When
        List<Hotel> fetch = queryFactory.selectFrom(hotel)
                .where(
                        provinceEq(request.getProvince())
                )
                .fetch();

        // Then
        Assertions.assertThat(fetch.size()).isEqualTo(locationMap.get(keyword));
    }

    @Test
    @DisplayName("최대 수용인원 조건에 따른 검색 테스트")
    public void searchByMaxCapacity() {
        // Given
        Integer requestMaxCapacity = rnd.nextInt(6, 16);
        createTestData(requestMaxCapacity);

        // When
        List<Long> ids = queryFactory.select(room.hotel.id)
                .from(room)
                .where(maxCapacityGt(requestMaxCapacity))
                .distinct().fetch();
        
        List<Hotel> fetch = queryFactory.selectFrom(hotel)
                .where(hotel.id.in(ids))
                .fetch();
        // Then
        Assertions.assertThat(fetch.size()).isEqualTo(confirmMaxCapacity);
    }

    private BooleanExpression maxCapacityGt(Integer requestMaxCapacity) {
        return requestMaxCapacity != null ? room.maxCapacity.gt(requestMaxCapacity) : null;
    }

    private BooleanExpression provinceEq(String province) {
        return province != null ? hotel.province.eq(province) : null;
    }

    public void createTestData(int maxCapacity) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        List<? extends Future<?>> futures = Stream.generate(
                () -> executorService.submit(() -> {
                    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
                    transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                        @Override
                        protected void doInTransactionWithoutResult(TransactionStatus status) {
                            String location = locations.get(rnd.nextInt(0, locations.size()));
                            locationMap.put(location, locationMap.get(location) + 10);
                            createHotels(10, location, "시흥시",
                                    "능곡동").stream().forEach(hotel -> {
                                List<Room> rooms = IntStream.rangeClosed(1, 5)
                                        .mapToObj(e -> {
                                            Room room = createRoom(hotel);
                                            createAmenity(room);
                                            return room;
                                        }).toList();
                                if (rooms.stream().filter(e -> e.getMaxCapacity() > maxCapacity).toList().size() > 0) {
                                    confirmMaxCapacity++;
                                }
                                roomDao.saveAll(rooms);
                            });
                        }
                    });
                })
        ).limit(10).toList();

        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private User createUser() {
        return User.builder()
                .email(randomUUID())
                .password(randomUUID())
                .build();
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
                .longitude(123.100)
                .mainImage("mainImg.url")
                .businessNumber("123456-123456")
                .certUrl("cert.url")
                .visitGuidance("가산디지털단지역 도보 5분")
                .user(user)
                .build();
    }

    public List<Hotel> createHotels(int idx, String province, String city, String district) {
        return IntStream.rangeClosed(1, idx)
                .mapToObj(e -> {
                    User user = createUser();
                    return createHotel(province, city, district, user);
                }).toList();
    }

    public Room createRoom(Hotel hotel) {
        return Room.builder()
                .hotel(hotel)
                .name(String.format("객실 이름_%s", randomUUID()))
                .description(String.format("객실 설명_%s", randomUUID()))
                .precaution(String.format("객실 주의사항_%s", randomUUID()))
                .totalRooms(rnd.nextInt(1, 100))
                .maxCapacity(rnd.nextInt(6, 16))
                .standardCapacity(rnd.nextInt(1, 5))
                .price(rnd.nextInt(500, 2000))
                .checkIn(LocalTime.of(15, 00, 0))
                .checkOut(LocalTime.of(11, 00, 00))
                .build();
    }

    private Pageable createPageable(int offset, int size) {
        return PageRequest.of(offset, size);
    }

    public String randomUUID() {
        return UUID.randomUUID().toString().substring(1, 11);
    }
}
