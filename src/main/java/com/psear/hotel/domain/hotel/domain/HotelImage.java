package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString(of = {"imageUrl"})
public class HotelImage extends BaseEntity {
    private String imageUrl;
    @ManyToOne(cascade = {
        CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Hotel hotel;

    @Builder
    public HotelImage(String imageUrl, Hotel hotel){
        this.imageUrl = imageUrl;
        setHotel(hotel);
    }

    public void setHotel(Hotel hotel){
        if(this.hotel!=null){
            this.hotel.removeImage(this);
        }
        this.hotel = hotel;
        this.hotel.addImage(this);
    }
}
