package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Reservation extends BaseEntity {
    private String roomId;
    private String userId;
    private String price;
    private String startAt;
    private String endAt;
    private String Field;
    private String adultCnt;
    private String childCnt;
    private String status;
}
