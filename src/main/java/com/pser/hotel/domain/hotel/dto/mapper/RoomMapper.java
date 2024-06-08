package com.pser.hotel.domain.hotel.dto.mapper;

import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.RoomImage;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import com.pser.hotel.domain.hotel.dto.response.RoomDetailResponse;
import com.pser.hotel.domain.hotel.dto.response.RoomListResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateRoomFromDto(RoomRequest request, @MappingTarget Room room);

    @Mapping(target = "heatingSystem", source = "room.amenity.heatingSystem")
    @Mapping(target = "tv", source = "room.amenity.tv")
    @Mapping(target = "refrigerator", source = "room.amenity.refrigerator")
    @Mapping(target = "airConditioner", source = "room.amenity.airConditioner")
    @Mapping(target = "washer", source = "room.amenity.washer")
    @Mapping(target = "terrace", source = "room.amenity.terrace")
    @Mapping(target = "coffeeMachine", source = "room.amenity.coffeeMachine")
    @Mapping(target = "internet", source = "room.amenity.internet")
    @Mapping(target = "kitchen", source = "room.amenity.kitchen")
    @Mapping(target = "bathtub", source = "room.amenity.bathtub")
    @Mapping(target = "iron", source = "room.amenity.iron")
    @Mapping(target = "pool", source = "room.amenity.pool")
    @Mapping(target = "pet", source = "room.amenity.pet")
    @Mapping(target = "inAnnex", source = "room.amenity.inAnnex")
    @Mapping(target = "roomImages", source = "roomImages")
    RoomDetailResponse roomToRoomDetailResponse(Room room);

    default List<String> mapRoomImages(List<RoomImage> roomImages) {
        return roomImages.stream()
                .map(RoomImage::getImageUrl)
                .collect(Collectors.toList());
    }

    @Mapping(target = "heatingSystem", source = "room.amenity.heatingSystem")
    @Mapping(target = "tv", source = "room.amenity.tv")
    @Mapping(target = "refrigerator", source = "room.amenity.refrigerator")
    @Mapping(target = "airConditioner", source = "room.amenity.airConditioner")
    @Mapping(target = "washer", source = "room.amenity.washer")
    @Mapping(target = "terrace", source = "room.amenity.terrace")
    @Mapping(target = "coffeeMachine", source = "room.amenity.coffeeMachine")
    @Mapping(target = "internet", source = "room.amenity.internet")
    @Mapping(target = "kitchen", source = "room.amenity.kitchen")
    @Mapping(target = "bathtub", source = "room.amenity.bathtub")
    @Mapping(target = "iron", source = "room.amenity.iron")
    @Mapping(target = "pool", source = "room.amenity.pool")
    @Mapping(target = "pet", source = "room.amenity.pet")
    @Mapping(target = "inAnnex", source = "room.amenity.inAnnex")
    RoomListResponse roomToRoomListResponse(Room room);
}
