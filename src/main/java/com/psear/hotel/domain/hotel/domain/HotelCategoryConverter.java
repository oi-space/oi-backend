package com.psear.hotel.domain.hotel.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class HotelCategoryConverter implements AttributeConverter<HotelCategoryEnum, String> {
    @Override
    public String convertToDatabaseColumn(HotelCategoryEnum achievementEnum) {
        return achievementEnum.getName();
    }

    @Override
    public HotelCategoryEnum convertToEntityAttribute(String value) {
        return HotelCategoryEnum.getByValue(value);
    }

}
