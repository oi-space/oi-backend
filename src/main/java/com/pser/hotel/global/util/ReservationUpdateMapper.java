package com.pser.hotel.global.util;

import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationUpdateRequestDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReservationUpdateMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReservationInfoFromRequest(ReservationUpdateRequestDto reservationUpdateRequestDto, @MappingTarget Reservation reservation);
}
