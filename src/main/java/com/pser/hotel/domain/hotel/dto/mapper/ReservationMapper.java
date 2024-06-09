package com.pser.hotel.domain.hotel.dto.mapper;

import com.pser.hotel.domain.hotel.domain.Amenity;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelImage;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.RoomImage;
import com.pser.hotel.domain.hotel.dto.AmenityDto;
import com.pser.hotel.domain.hotel.dto.FacilityDto;
import com.pser.hotel.domain.hotel.dto.HotelDto;
import com.pser.hotel.domain.hotel.dto.ReservationDto;
import com.pser.hotel.domain.hotel.dto.RoomDto;
import com.pser.hotel.domain.hotel.dto.request.ReservationCreateRequest;
import com.pser.hotel.domain.hotel.dto.response.ReservationResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    @Named("getImageUrls")
    default List<String> getImageUrls(List<HotelImage> hotelImages) {
        return hotelImages.stream().map(HotelImage::getImageUrl).toList();
    }

    @Named("getRoomImageUrls")
    default List<String> getRoomImageUrls(List<RoomImage> roomImages) {
        return roomImages.stream().map(RoomImage::getImageUrl).toList();
    }

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "room.id", target = "roomId")
    ReservationResponse toResponse(Reservation reservation);

    @Mapping(source = "room.price", target = "price")
    Reservation toEntity(ReservationCreateRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "room.hotel", target = "hotel")
    ReservationDto toDto(Reservation reservation);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "images", source = "images", qualifiedByName = "getImageUrls")
    HotelDto toDto(Hotel hotel);

    @Mapping(target = "hotelId", source = "hotel.id")
    FacilityDto toDto(Facility facility);

    @Mapping(target = "hotelId", source = "hotel.id")
    @Mapping(target = "roomImages", source = "roomImages", qualifiedByName = "getRoomImageUrls")
    RoomDto toDto(Room room);

    @Mapping(target = "roomId", source = "room.id")
    AmenityDto toDto(Amenity amenity);
}
