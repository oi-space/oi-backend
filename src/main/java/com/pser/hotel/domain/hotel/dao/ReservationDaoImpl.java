package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.QReservation;
import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationCreateRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationDaoImpl implements ReservationDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public int countOverlappingReservations(ReservationCreateRequest request) {
        QReservation reservation = QReservation.reservation;
        long count = Optional.ofNullable(queryFactory.select(reservation.count())
                        .where(
                                reservation.room.id.eq(request.getRoomId()),
                                buildOverlappingCondition(reservation, request),
                                matchScheduledStatusCondition(reservation, request)
                        )
                        .fetchOne())
                .orElse(0L);
        return (int) count;
    }

    private BooleanBuilder buildOverlappingCondition(QReservation reservation, ReservationCreateRequest request) {
        reservation.endAt.gt(request.getStartAt());
        return new BooleanBuilder()
                .andNot(matchReservedAfterRequest(reservation, request))
                .andNot(matchReservedBeforeRequest(reservation, request));
    }

    private Predicate matchReservedAfterRequest(QReservation reservation, ReservationCreateRequest request) {
        return reservation.startAt.goe(request.getEndAt());
    }

    private Predicate matchReservedBeforeRequest(QReservation reservation, ReservationCreateRequest request) {
        return reservation.endAt.goe(request.getStartAt());
    }

    private Predicate matchScheduledStatusCondition(QReservation reservation, ReservationCreateRequest request) {
        List<ReservationStatusEnum> notScheduledStatus = List.of(ReservationStatusEnum.AUCTION_SUCCESS,
                ReservationStatusEnum.REFUNDED, ReservationStatusEnum.PAST);
        return reservation.status.notIn(notScheduledStatus);
    }
}