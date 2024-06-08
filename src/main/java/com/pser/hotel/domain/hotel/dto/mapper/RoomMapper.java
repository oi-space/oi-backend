package com.pser.hotel.domain.hotel.dto.mapper;

import com.pser.hotel.domain.hotel.domain.Amenity;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.RoomImage;
import com.pser.hotel.domain.hotel.dto.AmenityDto;
import com.pser.hotel.domain.hotel.dto.RoomDto;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {
    @Named("getRoomImageUrls")
    default List<String> getRoomImageUrls(List<RoomImage> roomImages) {
        return roomImages.stream().map(RoomImage::getImageUrl).toList();
    }

    @Mapping(target = "hotelId", source = "hotel.id")
    @Mapping(target = "roomImages", source = "roomImages", qualifiedByName = "getRoomImageUrls")
    RoomDto toDto(Room room);

    @Mapping(target = "roomId", source = "room.id")
    AmenityDto toDto(Amenity amenity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateRoomFromDto(RoomRequest request, @MappingTarget Room room);
}
