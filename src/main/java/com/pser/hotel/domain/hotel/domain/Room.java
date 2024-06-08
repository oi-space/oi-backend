package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.WriteEventEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@Table(indexes = {
        @Index(name = "idx_room_hotel_id", columnList = "hotel_id"),
        @Index(name = "idx_room_price", columnList = "price"),
        @Index(name = "idx_room_name", columnList = "name"),
        @Index(name = "idx_room_description", columnList = "description"),
        @Index(name = "idx_room_precaution", columnList = "precaution"),
        @Index(name = "idx_room_standard_capacity", columnList = "standard_capacity"),
        @Index(name = "idx_room_max_capacity", columnList = "max_capacity"),
})
public class Room extends WriteEventEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Hotel hotel;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String description;

    private String mainImageUrl;

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
    public Room(Hotel hotel, String name, String description, String mainImageUrl, String precaution, int price,
                LocalTime checkIn,
                LocalTime checkOut, int standardCapacity, int maxCapacity, int totalRooms) {
        this.hotel = hotel;
        this.name = name;
        this.description = description;
        this.mainImageUrl = mainImageUrl;
        this.precaution = precaution;
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.standardCapacity = standardCapacity;
        this.maxCapacity = maxCapacity;
        this.totalRooms = totalRooms;
        this.roomImages = new ArrayList<>();
    }

    public void addImage(RoomImage roomImage) {
        this.roomImages.add(roomImage);
    }

    public void removeImage(RoomImage roomImage) {
        this.roomImages.remove(roomImage);
    }

    @PrePersist
    private void validate() {
        if (standardCapacity > maxCapacity) {
            throw new IllegalArgumentException("기준 인원이 최대 인원보다 클 수 없습니다");
        }
    }
}
