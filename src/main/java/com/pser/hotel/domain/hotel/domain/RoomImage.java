package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString(of = {"imageUrl"})
public class RoomImage extends BaseEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Room room;

    @Column(nullable = false)
    @URL
    private String imageUrl;

    @Builder
    public RoomImage(Room room, String imageUrl) {
        setRoom(room);
        this.imageUrl = imageUrl;
    }

    public void setRoom(Room room) {
        if (this.room != null) {
            this.room.removeImage(this);
        }
        this.room = room;
        this.room.addImage(this);
    }
}
