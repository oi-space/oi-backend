package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.model.BaseEntity;
import java.time.LocalDateTime;

public class TimeSale extends BaseEntity {
    //TODO: 객실 엔티티와 병합하면 객체 타입으로 변경
    private String room;
    private Integer price;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
