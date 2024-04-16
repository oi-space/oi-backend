package com.psear.hotel.domain.hotel.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class HotelCategoryConverter implements AttributeConverter<HotelCategoryEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(HotelCategoryEnum hotelCategoryEnum) {
        return hotelCategoryEnum.getValue();
    }

    @Override
    public HotelCategoryEnum convertToEntityAttribute(Integer value) {
        return HotelCategoryEnum.getByValue(value);
    }

}
