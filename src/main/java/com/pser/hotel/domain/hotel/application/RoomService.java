package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.domain.Amenity;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.RoomRequest;
import com.pser.hotel.domain.hotel.dto.RoomResponse;
import com.pser.hotel.domain.hotel.dto.RoomSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final HotelDao hotelDao;
    private final RoomDao roomDao;

    @Transactional
    public Page<RoomResponse> findRoomList(Pageable pageable) {
        Page<RoomResponse> result = roomDao.findAll(pageable).map(e -> e.toDto());
        return result;
    }

    @Transactional
    public RoomResponse findRoom(Long roomId) {
        return roomDao.findById(roomId).orElseThrow(() -> new IllegalArgumentException()).toDto();
    }

    @Transactional
    public Page<RoomResponse> search(RoomSearchRequest request, Pageable pageable) {
        return roomDao.search(request, pageable);
    }

    @Transactional
    public Long save(long userId, RoomRequest request) {
        Hotel hotel = findHotelByIdAndUserId(request.getHotelId(), userId);
        Room room = createRoom(hotel, request);
        Amenity amenity = createAmenity(room, request);
        roomDao.save(room);
        return room.getId();
    }

    @Transactional
    public void update(long userId, Long roomId, RoomRequest request) {
        Hotel hotel = findHotelByIdAndUserId(request.getHotelId(), userId);
        Room room = roomDao.findByIdAndHotelId(roomId, hotel.getId()).orElseThrow(() -> new IllegalArgumentException());
        room.update(request);
        roomDao.save(room);
    }

    @Transactional
    public void remove(long userId, Long hotelId, Long roomId) {
        Hotel hotel = findHotelByIdAndUserId(hotelId, userId);
        Room room = roomDao.findByIdAndHotelId(roomId, hotel.getId()).orElseThrow(() -> new IllegalArgumentException());
        roomDao.deleteById(room.getId());
    }

    private Hotel findHotelByIdAndUserId(Long hotelId, Long userId) {
        return hotelDao.findByIdAndUserId(hotelId, userId).orElseThrow(() -> new IllegalArgumentException());
    }

    private Amenity createAmenity(Room room, RoomRequest requestDto) {
        return Amenity.builder()
                .room(room)
                .heatingSystem(requestDto.getHeatingSystem())
                .tv(requestDto.getTv())
                .refrigerator(requestDto.getRefrigerator())
                .airConditioner(requestDto.getAirConditioner())
                .washer(requestDto.getWasher())
                .terrace(requestDto.getTerrace())
                .coffeeMachine(requestDto.getCoffeeMachine())
                .internet(requestDto.getInternet())
                .kitchen(requestDto.getKitchen())
                .bathtub(requestDto.getBathtub())
                .iron(requestDto.getIron())
                .pool(requestDto.getPool())
                .pet(requestDto.getPet())
                .inAnnex(requestDto.getInAnnex())
                .build();
    }

    private Room createRoom(Hotel hotel, RoomRequest requestDto) {
        Room room = Room.builder()
                .hotel(hotel)
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .precaution(requestDto.getPrecaution())
                .price(requestDto.getPrice())
                .checkIn(requestDto.getCheckIn())
                .checkOut(requestDto.getCheckOut())
                .standardCapacity(requestDto.getStandardCapacity())
                .maxCapacity(requestDto.getMaxCapacity())
                .totalRooms(requestDto.getTotalRooms())
                .build();
        return room;
    }
}
