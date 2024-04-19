package com.pser.hotel.domain.hotel.util;


import com.pser.hotel.domain.auction.domain.Auction;
import com.pser.hotel.domain.auction.domain.AuctionStatusEnum;
import com.pser.hotel.domain.auction.domain.Bid;
import com.pser.hotel.domain.hotel.domain.Amenity;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.ReservationEnum;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.member.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class Utils {
    private static Random rnd = new Random();

    public static Amenity createAmenity(Room room) {
        return Amenity.builder()
                .airConditioner(rnd.nextBoolean()).iron(rnd.nextBoolean())
                .pet(rnd.nextBoolean()).tv(rnd.nextBoolean()).pool(rnd.nextBoolean())
                .inAnnex(rnd.nextBoolean()).kitchen(rnd.nextBoolean())
                .bathtub(rnd.nextBoolean()).heatingSystem(rnd.nextBoolean())
                .coffeeMachine(rnd.nextBoolean()).refrigerator(rnd.nextBoolean()).terrace(rnd.nextBoolean())
                .internet(rnd.nextBoolean())
                .room(room)
                .build();
    }

    public static List<Room> createRooms(Hotel hotel, int count) {
        List<Room> result = new ArrayList<>();
        IntStream.rangeClosed(1, count)
                .forEach(e -> {
                    Room room = createRoom(hotel);
                    createAmenity(room);
                    result.add(room);
                });
        return result;
    }

    public static Room createRoom(Hotel hotel) {
        String uuid = UUID.randomUUID().toString().substring(1, 10);
        return Room.builder()
                .hotel(hotel)
                .name(String.format("객실 이름_%s", uuid))
                .description(String.format("객실 이름_%s", uuid))
                .precaution(String.format("객실 이름_%s", uuid))
                .totalRooms(new Random().nextInt(1, 100))
                .maxCapacity(new Random().nextInt(11, 20))
                .standardCapacity(new Random().nextInt(1, 10))
                .price(rnd.nextInt(500, 2000))
                .checkIn(LocalTime.of(15, 00, 0))
                .checkOut(LocalTime.of(11, 00, 00))
                .build();

    }

    public static User createUser() {
        String uuid = UUID.randomUUID().toString().substring(1, 10);
        return User.builder().email(String.format("이메일_%s", uuid)).password("123").build();
    }

    public static Hotel createHotel(User user) {
        String uuid = UUID.randomUUID().toString().substring(1, 10);
        Hotel hotel = Hotel.builder()
                .name(String.format("업체명_%s", uuid))
                .category(HotelCategoryEnum.HOTEL)
                .description(String.format("업체설명_%s", uuid))
                .notice(String.format("업체공자_%s", uuid))
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

    public static Reservation createReservation(User user, Room room) {
        return Reservation.builder()
                .price(1000)
                .startAt(LocalDate.of(2024, 4, 17))
                .endAt(LocalDate.of(2024, 4, 17))
                .reservationCapacity(5)
                .adultCapacity(3)
                .childCapacity(2)
                .status(ReservationEnum.BEFORE_ENTER_DEFAULT)
                .tid("test_tid")
                .user(user)
                .room(room)
                .build();
    }

    public static Reservation createReservation(User user, Room room, int reservationCapacity, int adultCapacity,
                                                int childCapacity) {
        return Reservation.builder()
                .price(1000)
                .startAt(LocalDate.of(2024, 4, 17))
                .endAt(LocalDate.of(2024, 4, 17))
                .reservationCapacity(reservationCapacity)
                .adultCapacity(adultCapacity)
                .childCapacity(childCapacity)
                .status(ReservationEnum.BEFORE_ENTER_DEFAULT)
                .tid("test_tid")
                .user(user)
                .room(room)
                .build();
    }

    public static Auction createAuction(Reservation auctionedReservation) {
        return Auction.builder()
                .auctionedReservation(auctionedReservation)
                .derivedReservation(null)
                .price(1000)
                .endPrice(2000)
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .depositPrice(1000)
                .status(AuctionStatusEnum.ON_GOING)
                .build();
    }

    public static Auction createAuction(Reservation auctionedReservation, int price, int endPrice) {
        return Auction.builder()
                .auctionedReservation(auctionedReservation)
                .derivedReservation(null)
                .price(price)
                .endPrice(endPrice)
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .depositPrice(1000)
                .status(AuctionStatusEnum.ON_GOING)
                .build();
    }

    public static Bid createBid(User user, Auction auction) {
        return Bid.builder()
                .user(user)
                .auction(auction)
                .price(1000)
                .build();
    }
}
