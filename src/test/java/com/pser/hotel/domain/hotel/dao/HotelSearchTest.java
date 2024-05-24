package com.pser.hotel.domain.hotel.dao;

import static com.pser.hotel.domain.hotel.domain.QHotel.hotel;
import static com.pser.hotel.domain.hotel.domain.QReservation.reservation;
import static com.pser.hotel.domain.hotel.domain.QRoom.room;
import static com.pser.hotel.domain.hotel.util.Utils.createAmenity;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
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
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
@DisplayName("Hotel 검색 테스트")
@Transactional
@Slf4j
public class HotelSearchTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    UserDao userDao;

    @Autowired
    RoomDao roomDao;

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    EntityManager em;

    Random rnd = new Random();

    List<Hotel> hotels = new ArrayList<>();

    List<Room> rooms = new ArrayList<>();

    List<Reservation> reservations = new ArrayList<>();

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
    @Transactional
    @DisplayName("keyword 조건에 따른 호텔 검색 테스트")
    public void searchTestByKeyword() {
        // Given
        String keyword = locations.get(rnd.nextInt(0, locations.size()));
        HotelSearchRequest request = HotelSearchRequest.builder()
                .keyword(keyword)
                .build();

        createHotelAndRoomData(10, 20);
        createReservationData(30);

        // When
        List<Hotel> fetch = queryFactory.select(hotel)
                .from(hotel)
                .where(
                        hotelIdIn(request),
                        provinceEq(request.getKeyword())
                )
                .fetch();

        // Then
        Set<Long> collect = hotels.stream()
                .filter(hotel -> hotel.getProvince().equals(request.getKeyword()))
                .map(hotel -> hotel.getId())
                .collect(Collectors.toSet());

        fetch.stream().forEach(hotel -> {
            Assertions.assertThat(collect.contains(hotel.getId())).isTrue();
        });
        Assertions.assertThat(collect.size()).isEqualTo(fetch.size());
    }

    @Test
    @Transactional
    @DisplayName("keyword, maxCapacity 조건에 따른 호텔 검색 테스트")
    public void searchTestByKeywordAndMaxCapacity() {
        // Given
        Integer requestMaxCapacity = rnd.nextInt(6, 16);

        String keyword = locations.get(rnd.nextInt(0, locations.size()));

        HotelSearchRequest request = HotelSearchRequest.builder()
                .keyword(keyword)
                .people(requestMaxCapacity)
                .build();

        createHotelAndRoomData(10, 20);
        createReservationData(30);

        // When
//        List<Long> ids = queryFactory.select(room.hotel.id)
//                .from(room)
//                .where(
//                        maxCapacityGt(request.getPeople())
//                )
//                .distinct().fetch();

        List<Hotel> fetch = queryFactory.select(hotel)
                .from(hotel)
                .where(
                        hotelIdIn(request),
                        provinceEq(request.getKeyword())
                )
                .fetch();

        // Then
        Set<Long> collect = rooms.stream()
                .filter(room -> room.getMaxCapacity() > request.getPeople())
                .map(room -> room.getHotel())
                .filter(hotel -> hotel.getProvince().equals(request.getKeyword()))
                .map(hotel -> hotel.getId())
                .collect(Collectors.toSet());

        fetch.stream().forEach(hotel -> {
            Assertions.assertThat(collect.contains(hotel.getId())).isTrue();
        });
        Assertions.assertThat(collect.size()).isEqualTo(fetch.size());
    }

    @Test
    @Transactional
    @DisplayName("keyword, searchStartAt, searchEndAt 조건에 따른 호텔 검색 테스트")
    public void searchTestByKeywordAndSearchStartAtAndSearchEndAt() {
        // Given
        String keyword = locations.get(rnd.nextInt(0, locations.size()));

        LocalDate startAt = getRandomDateBetween(LocalDate.of(2024, 4, 10), LocalDate.of(2024, 4, 25));
        LocalDate endAt = startAt.plusDays(2);

        HotelSearchRequest request = HotelSearchRequest.builder()
                .keyword(keyword)
                .searchStartAt(startAt)
                .searchEndAt(endAt)
                .build();

        createHotelAndRoomData(10, 20);
        createReservationData(30);

        // When
//        List<Long> ids = queryFactory.select(room.hotel.id)
//                .from(room)
//                .innerJoin(reservation)
//                .on(room.id.eq(reservation.room.id))
//                .where(
//                        searchCondition(request)
//                )
//                .distinct().fetch();

        List<Hotel> fetch = queryFactory.select(hotel)
                .from(hotel)
                .where(
                        hotelIdIn(request),
                        provinceEq(request.getKeyword())
                )
                .fetch();

        // Then
        Set<Long> collect = reservations.stream()
                .filter(reservation -> (reservation.getEndAt().isBefore(request.getSearchStartAt().plusDays(1))
                        || reservation.getStartAt().isAfter(request.getSearchEndAt())
                )).map(e -> e.getRoom())
                .map(e -> e.getHotel())
                .filter(e -> e.getProvince().equals(request.getKeyword()))
                .map(e -> e.getId())
                .collect(Collectors.toSet());

        fetch.stream().forEach(hotel -> {
            Assertions.assertThat(collect.contains(hotel.getId())).isTrue();
        });
        Assertions.assertThat(collect.size()).isEqualTo(fetch.size());
    }


    @Test
    @Transactional
    @DisplayName("keyword, maxCapacity, searchStartAt, searchEndAt 조건에 따른 호텔 검색 테스트")
    public void searchTestByKeywordAndMaxCapacityAndSearchStartAtAndSearchEndAt() {
        // Given
        Integer requestMaxCapacity = rnd.nextInt(6, 16);

        String keyword = locations.get(rnd.nextInt(0, locations.size()));

        LocalDate startAt = getRandomDateBetween(LocalDate.of(2024, 4, 10), LocalDate.of(2024, 4, 25));
        LocalDate endAt = startAt.plusDays(2);

        HotelSearchRequest request = HotelSearchRequest.builder()
                .keyword(keyword)
                .people(requestMaxCapacity)
                .searchStartAt(startAt)
                .searchEndAt(endAt)
                .build();

        createHotelAndRoomData(10, 20);
        createReservationData(30);

        // When
//        List<Long> ids = queryFactory.select(room.hotel.id)
//                .from(room)
//                .innerJoin(reservation)
//                .on(room.id.eq(reservation.room.id))
//                .where(
//                        searchCondition(request)
//                )
//                .distinct().fetch();

        List<Hotel> fetch = queryFactory.select(hotel)
                .from(hotel)
                .where(
                        hotelIdIn(request),
                        provinceEq(request.getKeyword())
                )
                .fetch();

        // Then
        Set<Long> collect = reservations.stream()
                .filter(reservation -> (reservation.getEndAt().isBefore(request.getSearchStartAt().plusDays(1))
                        || reservation.getStartAt().isAfter(request.getSearchEndAt())
                )).map(e -> e.getRoom())
                .filter(e -> e.getMaxCapacity() > request.getPeople())
                .map(e -> e.getHotel())
                .filter(e -> e.getProvince().equals(request.getKeyword()))
                .map(e -> e.getId())
                .collect(Collectors.toSet());

        fetch.stream().forEach(hotel -> {
            Assertions.assertThat(collect.contains(hotel.getId())).isTrue();
        });
        Assertions.assertThat(collect.size()).isEqualTo(fetch.size());
    }

    private Predicate hotelIdIn(HotelSearchRequest request) {
        if (request.getPeople() == null && (request.getSearchStartAt() == null || request.getSearchEndAt() == null)) {
            return null;
        } else if (request.getSearchStartAt() == null || request.getSearchEndAt() == null) {
            return hotel.id.in(
                    JPAExpressions.select(room.hotel.id)
                            .from(room)
                            .where(
                                    maxCapacityGt(request.getPeople())
                            )
                            .distinct()
            );
        }
        return hotel.id.in(
                JPAExpressions.select(room.hotel.id)
                        .from(room)
                        .innerJoin(reservation)
                        .on(room.id.eq(reservation.room.id))
                        .where(
                                searchCondition(request)
                        ).distinct()
        );
    }

    private BooleanBuilder searchCondition(HotelSearchRequest request) {
        return new BooleanBuilder()
                .and(maxCapacityGt(request.getPeople()))
                .and(searchDateBetween(request.getSearchStartAt(), request.getSearchEndAt()));
    }

    private BooleanExpression searchDateBetween(LocalDate startAt, LocalDate endAt) {
        return startAt != null && endAt != null ?
                reservation.endAt.loe(startAt)
                        .or(reservation.startAt.gt(endAt))
                : null;
    }

    private BooleanExpression maxCapacityGt(Integer requestMaxCapacity) {
        return requestMaxCapacity != null ? room.maxCapacity.gt(requestMaxCapacity) : null;
    }

    private BooleanExpression provinceEq(String province) {
        return province != null ? hotel.province.eq(province) : null;
    }

    // 호텔 생성 개수 = hotelCount * 10
    // 객실 생성 개수 = 호텔 생성 개수 * roomCount
    public void createHotelAndRoomData(int hotelCount, int roomCount) {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            String location = locations.get(rnd.nextInt(0, locations.size()));

            locationMap.put(location, locationMap.get(location) + hotelCount);

            createHotels(hotelCount, location, randomUUID(),
                    randomUUID()).stream().forEach(hotel -> {
                List<Room> saveRooms = IntStream.rangeClosed(1, roomCount)
                        .mapToObj(e -> {
                            Room room = createRoom(hotel);
                            createAmenity(room);
                            return room;
                        }).toList();
                rooms.addAll(saveRooms);
                roomDao.saveAll(saveRooms);
            });
        });
    }

    // idx 개수 만큼 Reservation 생성
    public void createReservationData(int idx) {
        IntStream.rangeClosed(1, idx).forEach(e -> {
            User user = userDao.findById(rnd.nextLong(1, 10)).get();
            Room room = roomDao.findById(rnd.nextLong(1, 2000)).get();

            LocalDate startAt = getRandomDateBetween(LocalDate.of(2024, 4, 10), LocalDate.of(2024, 4, 25));
            LocalDate endAt = startAt.plusDays(2);
            Reservation saveReservation = createReservation(user, room, startAt, endAt);
            reservationDao.save(saveReservation);
            reservations.add(saveReservation);
        });
    }

    public Reservation createReservation(User user, Room room, LocalDate startAt, LocalDate endAt) {
        return Reservation.builder()
                .price(1000)
                .startAt(startAt)
                .endAt(endAt)
                .visitorCount(5)
                .adultCount(2)
                .childCount(3)
                .user(user)
                .room(room)
                .build();
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
                .longtitude(123.100)
                .mainImage("mainImg.url")
                .businessNumber("123456-123456")
                .certUrl("cert.url")
                .visitGuidance("가산디지털단지역 도보 5분")
                .user(user)
                .build();
    }

    public List<Hotel> createHotels(int idx, String province, String city, String district) {
        List<Hotel> saveHotels = IntStream.rangeClosed(1, idx)
                .mapToObj(e -> {
                    User user = createUser();
                    return createHotel(province, city, district, user);
                }).toList();
        hotels.addAll(saveHotels);
        return saveHotels;
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

    public static LocalDate getRandomDateBetween(LocalDate startDate, LocalDate endDate) {
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay + 1);

        return LocalDate.ofEpochDay(randomEpochDay);
    }

    public String randomUUID() {
        return UUID.randomUUID().toString().substring(1, 11);
    }
}
