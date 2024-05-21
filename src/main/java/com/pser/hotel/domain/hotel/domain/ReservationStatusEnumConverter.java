package com.pser.hotel.domain.hotel.domain;

import jakarta.persistence.AttributeConverter;

public class ReservationStatusEnumConverter implements AttributeConverter<ReservationStatusEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ReservationStatusEnum reservationStatusEnum) {
        return reservationStatusEnum.getValue();
    }

    @Override
    public ReservationStatusEnum convertToEntityAttribute(Integer value) {
        return ReservationStatusEnum.getByValue(value);
    }
}
