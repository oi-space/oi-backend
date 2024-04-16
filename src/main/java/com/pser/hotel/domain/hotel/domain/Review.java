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
public class Review extends BaseEntity {
    private String grade;
    private String detail;
    private String rentId;

}
