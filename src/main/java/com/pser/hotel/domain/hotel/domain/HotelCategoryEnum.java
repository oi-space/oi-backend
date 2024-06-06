package com.pser.hotel.domain.hotel.domain;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum HotelCategoryEnum {
    HOTEL(0),
    PENSION(1),
    MOTEL(2),
    RESORT(3),
    CAMPING(4),
    GUESTHOUSE(5),
    KOREANHOUSE(6),
    POOLVILLA(7),
    DETACHEDHOUSE(8);

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
