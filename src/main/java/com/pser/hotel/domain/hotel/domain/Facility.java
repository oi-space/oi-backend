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
@ToString(of = {"parkingLot", "barbecue", "wifi", "sauna", "swimmingPool", "restaurant", "roofTop", "fitness", "dryer", "breakfast", "smokingArea", "allTimeDesk", "luggageStorage", "snackBar", "petFriendly"})
public class Facility extends BaseEntity {
    // TODO : 추후 추가 예정
    private Boolean parkingLot;
    private Boolean barbecue;
    private Boolean wifi;
    private Boolean sauna;
    private Boolean swimmingPool;
    private Boolean restaurant;
    private Boolean roofTop;
    private Boolean fitness;
    private Boolean dryer;
    private Boolean breakfast;
    private Boolean smokingArea;
    private Boolean allTimeDesk;
    private Boolean luggageStorage;
    private Boolean snackBar;
    private Boolean petFriendly;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Hotel hotel;

    @Builder
    public Facility(Boolean parkingLot, Boolean barbecue, Boolean wifi, Boolean sauna, Boolean swimmingPool,
        Boolean restaurant, Boolean roofTop, Boolean fitness, Boolean dryer, Boolean breakfast, Boolean smokingArea,
        Boolean allTimeDesk, Boolean luggageStorage, Boolean snackBar, Boolean petFriendly,
        Hotel hotel) {
        this.parkingLot = parkingLot;
        this.barbecue = barbecue;
        this.wifi = wifi;
        this.sauna = sauna;
        this.swimmingPool = swimmingPool;
        this.restaurant = restaurant;
        this.roofTop = roofTop;
        this.fitness = fitness;
        this.dryer = dryer;
        this.breakfast = breakfast;
        this.smokingArea = smokingArea;
        this.allTimeDesk = allTimeDesk;
        this.luggageStorage = luggageStorage;
        this.snackBar = snackBar;
        this.petFriendly = petFriendly;
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
