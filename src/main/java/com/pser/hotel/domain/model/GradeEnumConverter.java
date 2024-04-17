package com.pser.hotel.domain.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GradeEnumConverter implements AttributeConverter<GradeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(GradeEnum gradeEnum) {
        if (gradeEnum == null) {
            return null;
        }
        return gradeEnum.getValue();
    }

    @Override
    public GradeEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return GradeEnum.getByValue(dbData);
    }
}
