package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString(of = {"parkingLot", "barbecue", "wifi"})
public class Facility extends BaseEntity {
    // TODO : 추후 추가 예정
    private Boolean parkingLot;
    private Boolean barbecue;
    private Boolean wifi;
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Hotel hotel;

    @Builder
    public Facility(Boolean parkingLot, Boolean barbecue, Boolean wifi, Hotel hotel) {
        this.parkingLot = parkingLot;
        this.barbecue = barbecue;
        this.wifi = wifi;
        setHotel(hotel);
    }

    public void setHotel(Hotel hotel){
        if(this.hotel != null){
            this.hotel.setFacility(null);
        }
        this.hotel = hotel;
        this.hotel.setFacility(this);
    }
}
