package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.member.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ReservationDao extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r left join User u left join Room rm where u.email=:email")
    Page<Reservation> findByUserEmail(Pageable pageable, @Param("email") String email);
    @Query("select r from Reservation r left join User u left join Room rm where rm.name=:roomName")
    Optional<Reservation> findByRoomName(@Param("roomName") String roomName);

    @Query("select r from Reservation r left join Room rm where rm.name=:roomName and (r.startAt <= :endAt or r.endAt >= :startAt)")
    Optional<Reservation> findValidReservation(@Param("roomName") String roomName, @Param("startAt") LocalDate startAt, @Param("endAt")LocalDate endAt);
}
