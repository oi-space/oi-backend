package com.pser.hotel.domain.model;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum GradeEnum {

    ONE_STAR(1), // 별점 1
    TWO_STARS(2), // 별점 2
    THREE_STARS(3), // 별점 3
    FOUR_STARS(4), // 별점 4
    FIVE_STARS(5); // 별점 5


    private static final Map<Integer, GradeEnum> valueToGrade =
                Collections.unmodifiableMap(Stream.of(values())
                        .collect(Collectors.toMap(GradeEnum::getValue, Function.identity())));

        private final int value;

        GradeEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static GradeEnum getByValue(int value) {
            return valueToGrade.get(value);
        }
}
