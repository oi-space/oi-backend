package com.pser.hotel.domain.auction.domain;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Room;
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
@Import({QueryDslConfig.class})
@DisplayName("Bid 엔티티 테스트")
@Slf4j
class BidTest {
    @Autowired
    TestEntityManager testEntityManager;
    EntityManager em;
    User user;
    Hotel hotel;
    Room room;
    Reservation reservation;
    Auction auction;
    Bid bid;

    @BeforeEach
    public void setUp() {
        em = testEntityManager.getEntityManager();
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        room = Utils.createRoom(hotel);
        reservation = Utils.createReservation(user, room);
        auction = Utils.createAuction(reservation);
    }

    @AfterEach
    public void clear() {
    }

    @Test
    @DisplayName("Bid 저장 테스트")
    public void save() {
        // Given
        bid = Utils.createBid(user, auction);

        // When
        em.persist(bid);

        // Then
        Assertions.assertThat(em.contains(bid)).isTrue();
    }
}