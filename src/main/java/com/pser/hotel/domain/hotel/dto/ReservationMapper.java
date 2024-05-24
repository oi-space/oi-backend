package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationCreateRequest;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationUpdateRequestDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateReservationFromDto(ReservationUpdateRequestDto reservationUpdateRequestDto,
                                  @MappingTarget Reservation reservation);

    ReservationResponse toResponse(Reservation reservation);

    Reservation toEntity(ReservationCreateRequest request);
}
