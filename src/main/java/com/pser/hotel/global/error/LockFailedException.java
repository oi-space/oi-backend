package com.pser.hotel.global.error;

public class LockFailedException extends RuntimeException {
    public LockFailedException() {
        this("락 획득에 실패했습니다");
    }

    public LockFailedException(String message) {
        super(message);
    }
}
