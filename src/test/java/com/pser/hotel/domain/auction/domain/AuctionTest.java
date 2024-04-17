package com.pser.hotel.domain.auction.domain;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.ReservationEnum;
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
@DisplayName("Auction 엔티티 테스트")
@Slf4j
class AuctionTest {
    @Autowired
    TestEntityManager testEntityManager;
    EntityManager em;
    User user;
    Hotel hotel;
    Room room;
    Reservation reservation;
    Auction auction;

    @BeforeEach
    public void setUp() {
        em = testEntityManager.getEntityManager();
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        room = Utils.createRoom(hotel);
        reservation = Utils.createReservation(user, room);
    }

    @AfterEach
    public void clear() {
    }

    @Test
    @DisplayName("Auction 생성 테스트")
    public void save() {
        // Given
        auction = Utils.createAuction(reservation);

        // When
        em.persist(auction);

        // Then
        Assertions.assertThat(em.contains(auction)).isTrue();
        Assertions.assertThat(auction.getAuctionedReservation()).isNotNull();
        Assertions.assertThat(auction.getDerivedReservation()).isNull();
    }

    @Test
    @DisplayName("경매되어 생긴 예약 업데이트 테스트")
    public void updateDerivedReservation() {
        // Given
        auction = Utils.createAuction(reservation);
        em.persist(auction);
        em.flush();
        Reservation derivedReservation = Utils.createReservation(user, room);
        derivedReservation.setStatus(ReservationEnum.AUCTION_PROCESSING);
        Auction findAuction = (Auction) em.createQuery("SELECT a FROM Auction a WHERE a.id = :auctionId")
                .setParameter("auctionId", auction.getId()).getSingleResult();
        findAuction.success(derivedReservation);

        // When
        em.persist(findAuction);

        // Then
        Assertions.assertThat(findAuction.getAuctionedReservation()).isNotNull();
        Assertions.assertThat(findAuction.getDerivedReservation()).isNotNull();
        Assertions.assertThat(findAuction.getDerivedReservation().getStatus().getValue())
                .isEqualTo(ReservationEnum.AUCTION_SUCCESS.getValue());
    }

    @Test
    @DisplayName("Auction 삭제테스트")
    public void remove() {
        // Given
        auction = Utils.createAuction(reservation);
        em.persist(auction);

        // When
        em.remove(auction);

        // Then
        Assertions.assertThat(em.contains(auction)).isFalse();
    }

    @Test
    @DisplayName("낙찰가가 경매 시작가보다 작으면 IllegalArgument예외를 던진다.")
    public void validate() {
        // Given
        auction = Utils.createAuction(reservation, 2000, 1000);

        //When
        Throwable throwable = Assertions.catchThrowable(() -> {
            em.persist(auction);
        });

        //Then
        Assertions.assertThat(throwable).isInstanceOf(IllegalArgumentException.class);

    }
}