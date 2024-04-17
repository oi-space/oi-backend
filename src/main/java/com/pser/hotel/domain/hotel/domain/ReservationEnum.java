package com.pser.hotel.domain.hotel.domain;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum ReservationEnum {
    BEFORE_ENTER(0),
    AUCTION_PROCESSING(1),
    REFUND(3),
    AUCTION_SUCCESS(4);
    
    private static final Map<Integer, ReservationEnum> valueToName =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(ReservationEnum::getValue, Function.identity())));

    private final Integer value;

    ReservationEnum(Integer value) {
        this.value = value;
    }

    public static ReservationEnum getByValue(Integer value) {
        return valueToName.get(value);
    }
}
