package com.psear.hotel.domain.hotel.domain;

import com.psear.hotel.domain.model.BaseEntity;
import jakarta.persistence.Column;
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
public class Facility extends BaseEntity {
    private Boolean parkingLot;
    private Boolean barbecue;
    private Boolean wifi;
}
