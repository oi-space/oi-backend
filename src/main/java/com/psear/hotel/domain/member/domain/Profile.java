package com.psear.hotel.domain.member.domain;

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
public class Profile extends BaseEntity {
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private String phone;
}
