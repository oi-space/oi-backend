package com.pser.hotel.domain.hotel.dto.mapper;

import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateRoomFromDto(RoomRequest request, @MappingTarget Room room);
}
