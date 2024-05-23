package com.pser.hotel.domain.hotel.domain;

import com.pser.hotel.domain.hotel.config.MapperConfig;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class, MapperConfig.class})
@DisplayName("Reservation 엔티티 테스트")
@Slf4j
class ReservationTest {
    @Autowired
    TestEntityManager testEntityManager;
    EntityManager em;
    User user;
    Hotel hotel;
    Room room;
    Reservation reservation;

    @BeforeEach
    public void setUp() {
        em = testEntityManager.getEntityManager();
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        room = Utils.createRoom(hotel);
    }

    @AfterEach
    public void clear() {
    }

    @Test
    @DisplayName("Reservation 생성 테스트")
    public void save() {
        // Given
        reservation = Utils.createReservation(user, room);

        // When
        em.persist(reservation);

        // Then
        Assertions.assertThat(em.contains(reservation)).isTrue();
        Assertions.assertThat(reservation.getUser().getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(reservation.getRoom().getName()).isEqualTo(room.getName());
    }

    @Test
    @DisplayName("성인인원과 아동인원의 합이 예약인원과 다르면 IllegalArgument예외를 던진다.")
    public void validate() {
        // Given
        reservation = Utils.createReservation(user, room, 4, 1, 2);

        //When
        Throwable throwable = Assertions.catchThrowable(() -> {
            em.persist(reservation);
        });

        //Then
        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);

    }
}