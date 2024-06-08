package com.pser.hotel.domain.member.dao;

import com.pser.hotel.domain.member.domain.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileDao extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(long userId);
}
