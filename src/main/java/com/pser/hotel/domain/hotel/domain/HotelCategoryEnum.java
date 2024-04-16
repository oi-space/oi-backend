package com.pser.hotel.domain.hotel.domain;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum HotelCategoryEnum {
    // TODO : 추후 추가 예정
    HOTEL(0),
    PENSION(1);

    private static final Map<Integer, HotelCategoryEnum> valueToName =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(HotelCategoryEnum::getValue, Function.identity())));

    private final Integer value;

    HotelCategoryEnum(Integer value){
        this.value = value;
    }

    public static HotelCategoryEnum getByValue(Integer value){
        return valueToName.get(value);
    }
}
