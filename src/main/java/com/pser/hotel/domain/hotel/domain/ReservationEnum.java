package com.pser.hotel.domain.hotel.domain;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum ReservationEnum {
    END_RESERVATION(0),    // 종료된 예약
    BEFORE_ENTER_DEFAULT(1), // 입장 전
    BEFORE_ENTER_DISQUALIFICATION(2),
    REFUND(3), // 환불
    AUCTION_PROCESSING(4),  // 경매 중
    AUCTION_SUCCESS(5); // 경매 성공

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
