package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.hotel.dto.RoomRequest;
import com.pser.hotel.domain.hotel.dto.RoomResponse;
import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "room", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RoomImage> roomImages = new ArrayList<>();

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
        this.roomImages = new ArrayList<>();
    }

    @PrePersist
    private void validate() {
        if (standardCapacity > maxCapacity) {
            throw new IllegalArgumentException("기준 인원이 최대 인원보다 클 수 없습니다");
        }
    }

    public RoomResponse toDto() {
        return RoomResponse.builder()
                .name(this.name)
                .build();
    }

    public void addImage(RoomImage roomImage) {
        this.roomImages.add(roomImage);
    }

    public void removeImage(RoomImage roomImage) {
        this.roomImages.remove(roomImage);
    }

    public void update(RoomRequest request) {
        if (!request.getName().equals(name)) {
            this.name = request.getName();
        }
        if (!request.getDescription().equals(description)) {
            this.description = request.getDescription();
        }
        if (!request.getPrecaution().equals(precaution)) {
            this.precaution = request.getPrecaution();
        }
        if (!(request.getPrice() == price)) {
            this.price = request.getPrice();
        }
        if (!request.getCheckIn().equals(request.getCheckIn())) {
            this.checkIn = request.getCheckIn();
        }
        if (!request.getCheckOut().equals(request.getCheckOut())) {
            this.checkOut = request.getCheckOut();
        }
        if (!(request.getStandardCapacity() == standardCapacity)) {
            this.standardCapacity = request.getStandardCapacity();
        }
        if (!(request.getMaxCapacity() == maxCapacity)) {
            this.maxCapacity = request.getMaxCapacity();
        }
        if (!(request.getTotalRooms() == totalRooms)) {
            this.totalRooms = request.getTotalRooms();
        }
    }
}
