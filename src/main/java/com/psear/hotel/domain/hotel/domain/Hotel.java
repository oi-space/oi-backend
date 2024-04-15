package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.member.domain.User;
import com.psear.hotel.domain.model.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
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
@ToString(of = {"name", "category", "description", "notice", "province", "city", "district", "detailedAddress", "latitude", "longtitude", "businessNumber"})
public class Hotel extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    @Convert(converter = HotelCategoryConverter.class)
    private HotelCategoryEnum category;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String notice;
    @Column(nullable = false)
    private String province;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private String detailedAddress;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longtitude;
    @Column(nullable = false)
    private String mainImage;
    @Column(nullable = false)
    private String businessNumber;
    @Column(nullable = false)
    private String certUrl;
    @Column(nullable = false)
    private String visitGuidance;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @OneToOne(mappedBy = "hotel", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private Facility facility ;

    @OneToMany(mappedBy = "hotel", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<HotelImage> imageList = new ArrayList<>();

    public void addImage(HotelImage image){
        this.imageList.add(image);
    }

    public void removeImage(HotelImage image){
        this.imageList.remove(image);
    }

    public void setFacility(Facility facility){
        if(this.facility!= null){
            this.facility.removeHotel();
        }
        facility.setHotel(this);
        this.facility = facility;
    }
}
