package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Room extends BaseEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Hotel hotel;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String description;

    private String precaution;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private LocalTime checkIn;

    @Column(nullable = false)
    private LocalTime checkOut;

    @Column(nullable = false)
    @Min(0)
    private int standardCapacity;

    @Column(nullable = false)
    @Min(0)
    private int maxCapacity;

    @Column(nullable = false)
    @Min(0)
    private int totalRooms;

    @OneToOne(optional = false, mappedBy = "room", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private Amenity amenity;

    @Builder
    public Room(Hotel hotel, String name, String description, String precaution, int price, LocalTime checkIn,
                LocalTime checkOut, int standardCapacity, int maxCapacity, int totalRooms) {
        this.hotel = hotel;
        this.name = name;
        this.description = description;
        this.precaution = precaution;
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.standardCapacity = standardCapacity;
        this.maxCapacity = maxCapacity;
        this.totalRooms = totalRooms;
    }

    @PrePersist
    private void validate() {
        if (standardCapacity > maxCapacity) {
            throw new IllegalArgumentException("기준 인원이 최대 인원보다 클 수 없습니다");
        }
    }
}
