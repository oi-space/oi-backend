package com.pser.hotel.domain.hotel.util;


import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.member.domain.User;
import java.time.LocalTime;
import java.util.Random;
import java.util.UUID;

public class Utils {

    public static Room createRoom() {
        String uuid = UUID.randomUUID().toString().substring(1, 10);
        return Room.builder()
                .hotel(createHotel())
                .name(String.format("객실 이름_%s", uuid))
                .description(String.format("객실 이름_%s", uuid))
                .precaution(String.format("객실 이름_%s", uuid))
                .totalRooms(new Random().nextInt(1, 100))
                .maxCapacity(new Random().nextInt(3, 10))
                .standardCapacity(new Random().nextInt(1, 3))
                .price(1000)
                .checkIn(LocalTime.of(15, 00, 0))
                .checkOut(LocalTime.of(11, 00, 00))
                .build();

    }

    public static User createUser() {
        String uuid = UUID.randomUUID().toString().substring(1, 10);
        return User.builder().email(String.format("이메일_%s", uuid)).password("123").build();
    }

    public static Hotel createHotel() {
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
                .user(createUser())
                .build();
        return hotel;
    }
}
