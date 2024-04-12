package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.model.BaseEntity;

public class Amenity extends BaseEntity {
    //TODO: 객실 엔티티와 병합하면 객체 타입으로 변경
    private String room;
    private boolean heatingSystem = false;
    private boolean tv = false;
    private boolean refrigerator = false;
    private boolean airConditioner = false;
    private boolean washer = false;
    private boolean terrace = false;
    private boolean coffeeMachine = false;
    private boolean internet = false;
    private boolean kitchen = false;
    private boolean bathtub = false;
    private boolean iron = false;
    private boolean pool = false;
    private boolean pet = false;
    private boolean inAnnex = false;
}
