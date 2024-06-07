package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.domain.model.StatusHolderEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.Id;


@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString(of = {"price", "startAt", "endAt", "visitorCount", "adultCount", "childCount", "status"})
@Table(indexes = {@Index(name = "idx_reservation_room_id", columnList = "room_id")})
public class Reservation extends StatusHolderEntity<ReservationStatusEnum> {
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Room room;

    @Column(unique = true, nullable = false)
    private String merchantUid = UUID.randomUUID().toString();

    @Column(unique = true)
    private String impUid;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

    @Column(nullable = false)
    @Min(0)
    private int visitorCount;

    @Column(nullable = false)
    @Min(0)
    private int adultCount;

    @Column(nullable = false)
    @Min(0)
    private int childCount;

    @Column(nullable = false)
    @Convert(converter = ReservationStatusEnumConverter.class)
    private ReservationStatusEnum status = ReservationStatusEnum.CREATED;

    @Builder
    public Reservation(User user, Room room, int price, LocalDate startAt, LocalDate endAt,
                       int visitorCount,
                       int adultCount, int childCount) {
        setUser(user);
        setRoom(room);
        this.price = price;
        this.startAt = startAt;
        this.endAt = endAt;
        this.visitorCount = visitorCount;
        this.adultCount = adultCount;
        this.childCount = childCount;
    }

    public void updateImpUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    @PrePersist
    private void validate() {
        if (visitorCount != (adultCount + childCount)) {
            throw new IllegalArgumentException("총원이 맞지 않습니다");
        }
    }

}
