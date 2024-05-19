package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.QHotel;
import com.pser.hotel.domain.hotel.domain.QRoom;
import com.pser.hotel.domain.hotel.domain.QTimeSale;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TimesaleDaoImpl implements TimesaleCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Hotel> findHotelByRoomId(Long roomId) {
        QHotel hotel = QHotel.hotel;
        QRoom room = QRoom.room;

        Hotel result = queryFactory.select(hotel)
                .from(room)
                .join(room.hotel, hotel)
                .where(room.id.eq(roomId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Hotel> findHotelByTimesaleId(Long timeSaleId) {
        QTimeSale timeSale = QTimeSale.timeSale;
        QRoom room = QRoom.room;
        QHotel hotel = QHotel.hotel;

        Hotel result = queryFactory.select(hotel)
                .from(timeSale)
                .join(timeSale.room, room)
                .join(room.hotel, hotel)
                .where(timeSale.id.eq(timeSaleId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
