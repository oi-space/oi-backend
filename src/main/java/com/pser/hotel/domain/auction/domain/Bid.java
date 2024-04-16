package com.pser.hotel.domain.auction.domain;

import com.pser.hotel.domain.model.BaseEntity;
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
public class Bid extends BaseEntity {
    private String userId;
    private String auctionId;
    private String price;

}
