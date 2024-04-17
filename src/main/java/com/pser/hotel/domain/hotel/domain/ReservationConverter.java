package com.pser.hotel.domain.hotel.domain;

import jakarta.persistence.AttributeConverter;

public class ReservationConverter implements AttributeConverter<ReservationEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ReservationEnum reservationEnum) {
        return reservationEnum.getValue();
    }

    @Override
    public ReservationEnum convertToEntityAttribute(Integer value) {
        return ReservationEnum.getByValue(value);
    }
}
