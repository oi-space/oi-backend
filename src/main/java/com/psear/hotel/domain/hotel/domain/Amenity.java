package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Room room;

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

    @Builder
    public Amenity(Room room, Boolean heatingSystem, Boolean tv, Boolean refrigerator, Boolean airConditioner,
                   Boolean washer, Boolean terrace, Boolean coffeeMachine, Boolean internet, Boolean kitchen,
                   Boolean bathtub, Boolean iron, Boolean pool, Boolean pet, Boolean inAnnex) {
        this.room = room;
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
}
