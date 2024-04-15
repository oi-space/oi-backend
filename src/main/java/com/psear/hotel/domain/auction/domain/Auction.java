package com.psear.hotel.domain.auction.domain;

import com.psear.hotel.domain.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Auction extends BaseEntity {
    private String auctionedReservationId;
    private String derivedReservationId;
    private String price;
    private String endPrice;
    private String startAt;
    private String endAt;
    private String depositPrice;
    private String status;

}
