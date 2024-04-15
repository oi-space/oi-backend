package com.psear.hotel.domain.member.domain;

import com.psear.hotel.domain.model.BaseEntity;
import com.psear.hotel.domain.model.RoleEnum;
import com.psear.hotel.domain.model.RoleEnumConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Convert(converter = RoleEnumConverter.class)
    private RoleEnum role = RoleEnum.ROLE_USER;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST,
            CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private Profile profile ;

    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}