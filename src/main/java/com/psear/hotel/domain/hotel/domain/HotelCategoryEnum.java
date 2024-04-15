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
    호텔("hotel"),
    펜션("pension");

    private static final Map<String, HotelCategoryEnum> valueToName =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(HotelCategoryEnum::getName, Function.identity())));

    private final String name;

    HotelCategoryEnum(String name){
        this.name = name;
    }

    public static HotelCategoryEnum getByValue(String value){
        return valueToName.get(value);
    }
}
