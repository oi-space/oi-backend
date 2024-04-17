package com.pser.hotel.domain.auction.domain;

import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "auction_id"}
                )
        }
)
@Builder
@AllArgsConstructor
public class Bid extends BaseEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Auction auction;
    private int price;
}
