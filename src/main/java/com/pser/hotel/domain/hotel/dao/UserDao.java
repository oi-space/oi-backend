package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
}
