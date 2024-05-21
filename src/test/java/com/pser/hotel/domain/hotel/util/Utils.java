package com.pser.hotel.domain.hotel.util;


import com.pser.hotel.domain.hotel.domain.Amenity;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.RoomImage;
import com.pser.hotel.domain.member.domain.User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class Utils {
    private static Random rnd = new Random();

    public static String createImageUrl() {
        return String.format("http://%s.com", UUID.randomUUID().toString().substring(1, 10));
    }

    public static RoomImage createRoomImage(Room room, String imageUrl) {
        return RoomImage.builder().room(room).imageUrl(imageUrl).build();
    }

    public static List<RoomImage> createRoomImages(Room room, List<String> imageUrls) {
        return imageUrls.stream().map(url -> createRoomImage(room, url)).toList();
    }

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

    public static List<Hotel> createHotels(User user, int count) {
        List<Hotel> list = new ArrayList<>();
        for(int i=0; i < count; i++){
            Hotel hotel = createHotel(user);
            Facility facility = createFacility(hotel);
            list.add(hotel);
        }
        return list;
    }

    public static Facility createFacility(Hotel hotel){
        return Facility.builder()
            .hotel(hotel)
            .parkingLot(rnd.nextBoolean())
            .wifi(rnd.nextBoolean())
            .barbecue(rnd.nextBoolean())
            .sauna(rnd.nextBoolean())
            .swimmingPool(rnd.nextBoolean())
            .restaurant(rnd.nextBoolean())
            .roofTop(rnd.nextBoolean())
            .fitness(rnd.nextBoolean())
            .dryer(rnd.nextBoolean())
            .breakfast(rnd.nextBoolean())
            .smokingArea(rnd.nextBoolean())
            .allTimeDesk(rnd.nextBoolean())
            .luggageStorage(rnd.nextBoolean())
            .snackBar(rnd.nextBoolean())
            .petFriendly(rnd.nextBoolean())
            .build();
    }

    public static List<String> createHotelImages(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=1; i <= 3; i++) {
            String uuid = UUID.randomUUID().toString().substring(1, 10);
            arrayList.add(String.format("image_%s", uuid));
        }
        return arrayList;
    }

    public static Reservation createReservation(User user, Room room) {
        return Reservation.builder()
                .price(1000)
                .startAt(LocalDate.of(2024, 4, 17))
                .endAt(LocalDate.of(2024, 4, 17))
                .reservationCapacity(5)
                .adultCapacity(3)
                .childCapacity(2)
                .status(ReservationStatusEnum.BEFORE_CHECKIN)
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
                .status(ReservationStatusEnum.BEFORE_CHECKIN)
                .user(user)
                .room(room)
                .build();
    }
}
