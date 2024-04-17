package com.pser.hotel.domain.model;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum GradeEnum {

        EXCELLENT(0), // 우수
        GOOD(1), // 좋음
        AVERAGE(2), // 보통
        POOR(3); // 나쁨

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
