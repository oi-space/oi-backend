package com.pser.hotel.domain.model;

public interface StatusHolder<T extends StatusEnum> {
    void updateStatus(T status);
}
