package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.domain.Amenity;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.RoomImage;
import com.pser.hotel.domain.hotel.dto.mapper.RoomMapper;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import com.pser.hotel.domain.hotel.dto.response.RoomResponse;
import com.pser.hotel.global.error.UserNotAllowedException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public Page<RoomResponse> findRoomList(Pageable pageable) {
        Page<RoomResponse> result = roomDao.findAll(pageable).map(room -> roomMapper.roomToRoomResponse(room));
        return result;
    }

    @Transactional(readOnly = true)
    public RoomResponse findRoom(Long roomId) {
        Room room = roomDao.findById(roomId).orElseThrow(() -> new IllegalArgumentException());

        return roomMapper.roomToRoomResponse(room);
    }

    @Transactional
    public Long save(long userId, long hotelId, RoomRequest request) {
        Hotel hotel = findHotelByIdAndUserId(hotelId, userId);
        Room room = createRoom(hotel, request);
        Amenity amenity = createAmenity(room, request);
        List<RoomImage> roomImages = createRoomImages(room, request.getImgUrls());
        roomDao.save(room);
        return room.getId();
    }

    private Hotel findHotelByIdAndUserId(long hotelId, long userId) {
        return hotelDao.findByIdAndUserId(hotelId, userId).orElseThrow(() -> new UserNotAllowedException());
    }

    @Transactional
    public void update(long userId, long hotelId, long roomId, RoomRequest request) {
        Hotel hotel = findHotelByIdAndUserId(hotelId, userId);
        Room room = findRoomByIdAndHoteId(roomId, hotel.getId());
        roomMapper.updateRoomFromDto(request, room);
        roomDao.save(room);
    }

    @Transactional
    public void remove(long userId, Long hotelId, Long roomId) {
        Hotel hotel = findHotelByIdAndUserId(hotelId, userId);
        Room room = findRoomByIdAndHoteId(roomId, hotel.getId());
        roomDao.deleteById(room.getId());
    }

    private Room findRoomByIdAndHoteId(Long roomId, Long hotelId) {
        return roomDao.findByIdAndHotelId(roomId, hotelId).orElseThrow(() -> new EntityNotFoundException());
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

    private RoomImage createRoomImage(Room room, String imageUrl) {
        return RoomImage.builder()
                .imageUrl(imageUrl)
                .room(room)
                .build();
    }

    private List<RoomImage> createRoomImages(Room room, List<String> imgUrls) {
        return imgUrls.stream().map(url -> createRoomImage(room, url)).toList();
    }
}
