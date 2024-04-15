package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.model.BaseEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {"parkingLot", "barbecue", "wifi"})
public class Facility extends BaseEntity {
    // TODO : 추후 추가 예정
    private Boolean parkingLot;
    private Boolean barbecue;
    private Boolean wifi;
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Hotel hotel;

    public void removeHotel() {
        hotel = null;
    }
}
