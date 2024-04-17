package com.pser.hotel.domain.auction.domain;

import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.ReservationEnum;
import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"auctioned_reservation_id", "derived_reservation_id"}
                )
        }
)
@ToString(of = {"price", "endPrice", "startAt", "endAt", "depositPrice", "status"})
public class Auction extends BaseEntity {
    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reservation auctionedReservation;
    @OneToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reservation derivedReservation;
    private int price;
    private int endPrice;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private int depositPrice;
    private AuctionStatusEnum status;

    @Builder
    public Auction(Reservation auctionedReservation, Reservation derivedReservation, int price, int endPrice,
                   LocalDateTime startAt, LocalDateTime endAt, int depositPrice, AuctionStatusEnum status) {
        setAuctionedReservation(auctionedReservation);
        setDerivedReservation(derivedReservation);
        this.price = price;
        this.endPrice = endPrice;
        this.startAt = startAt;
        this.endAt = endAt;
        this.depositPrice = depositPrice;
        this.status = status;
    }

    @PrePersist
    private void validate() {
        if (price > endPrice) {
            throw new IllegalArgumentException("낙찰가가 경매 시작가보다 클 수 없습니다.");
        }
    }

    public void success(Reservation derivedReservation) {
        this.derivedReservation = derivedReservation;
        this.derivedReservation.setStatus(ReservationEnum.AUCTION_SUCCESS);
    }
}
