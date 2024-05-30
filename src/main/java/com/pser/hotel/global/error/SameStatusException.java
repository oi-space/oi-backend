package com.pser.hotel.global.error;

public class SameStatusException extends RuntimeException {
    public SameStatusException() {
        this("이미 해당 상태에 있습니다");
    }

    public SameStatusException(String message) {
        super(message);
    }
}
