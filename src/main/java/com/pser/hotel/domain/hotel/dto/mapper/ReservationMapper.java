package com.pser.hotel.domain.hotel.dto.mapper;

import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.dto.ReservationDto;
import com.pser.hotel.domain.hotel.dto.request.ReservationCreateRequest;
import com.pser.hotel.domain.hotel.dto.response.ReservationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "room.id", target = "roomId")
    ReservationResponse toResponse(Reservation reservation);

    Reservation toEntity(ReservationCreateRequest request);

    @Mapping(source = "room.checkIn", target = "checkIn")
    @Mapping(source = "room.checkOut", target = "checkOut")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "room.id", target = "roomId")
    ReservationDto toDto(Reservation reservation);
}
