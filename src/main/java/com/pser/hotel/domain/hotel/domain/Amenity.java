package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Amenity extends BaseEntity {
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Room room;

    private Boolean heatingSystem = false;

    private Boolean tv = false;

    private Boolean refrigerator = false;

    private Boolean airConditioner = false;

    private Boolean washer = false;

    private Boolean terrace = false;

    private Boolean coffeeMachine = false;

    private Boolean internet = false;

    private Boolean kitchen = false;

    private Boolean bathtub = false;

    private Boolean iron = false;

    private Boolean pool = false;

    private Boolean pet = false;

    private Boolean inAnnex = false;

    @Builder
    public Amenity(Room room, Boolean heatingSystem, Boolean tv, Boolean refrigerator, Boolean airConditioner,
                   Boolean washer, Boolean terrace, Boolean coffeeMachine, Boolean internet, Boolean kitchen,
                   Boolean bathtub, Boolean iron, Boolean pool, Boolean pet, Boolean inAnnex) {
        setRoom(room);
        this.heatingSystem = Optional.ofNullable(heatingSystem).orElse(this.heatingSystem);
        this.tv = Optional.ofNullable(tv).orElse(this.tv);
        this.refrigerator = Optional.ofNullable(refrigerator).orElse(this.refrigerator);
        this.airConditioner = Optional.ofNullable(airConditioner).orElse(this.airConditioner);
        this.washer = Optional.ofNullable(washer).orElse(this.washer);
        this.terrace = Optional.ofNullable(terrace).orElse(this.terrace);
        this.coffeeMachine = Optional.ofNullable(coffeeMachine).orElse(this.coffeeMachine);
        this.internet = Optional.ofNullable(internet).orElse(this.internet);
        this.kitchen = Optional.ofNullable(kitchen).orElse(this.kitchen);
        this.bathtub = Optional.ofNullable(bathtub).orElse(this.bathtub);
        this.iron = Optional.ofNullable(iron).orElse(this.iron);
        this.pool = Optional.ofNullable(pool).orElse(this.pool);
        this.pet = Optional.ofNullable(pet).orElse(this.pet);
        this.inAnnex = Optional.ofNullable(inAnnex).orElse(this.inAnnex);
    }

    public void setRoom(Room room) {
        room.setAmenity(this);
        this.room = room;
    }
}
