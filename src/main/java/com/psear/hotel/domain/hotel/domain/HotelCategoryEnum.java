package com.psear.hotel.domain.hotel.domain;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum HotelCategoryEnum {
    // TODO : 추후 추가 예정
    호텔(0),
    펜션(1);

    private static final Map<Integer, HotelCategoryEnum> valueToName =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(HotelCategoryEnum::getName, Function.identity())));

    private final Integer name;

    HotelCategoryEnum(Integer name){
        this.name = name;
    }

    public static HotelCategoryEnum getByValue(Integer value){
        return valueToName.get(value);
    }
}
