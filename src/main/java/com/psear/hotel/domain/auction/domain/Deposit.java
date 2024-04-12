package com.psear.hotel.domain.auction.domain;

import com.psear.hotel.domain.member.domain.User;
import com.psear.hotel.domain.model.BaseEntity;

public class Deposit extends BaseEntity {
    private User user;
    //TODO: 경매 엔티티와 병합하면 객체 타입으로 변경
    private String auction;
}
