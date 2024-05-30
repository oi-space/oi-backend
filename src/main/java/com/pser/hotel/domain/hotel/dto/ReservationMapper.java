package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationCreateRequest;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationUpdateRequestDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReservationInfoFromRequest(ReservationUpdateRequestDto reservationUpdateRequestDto,
                                          @MappingTarget Reservation reservation);

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
