package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.QHotel;
import com.pser.hotel.domain.hotel.domain.QRoom;
import com.pser.hotel.domain.hotel.domain.QTimeSale;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import com.pser.hotel.domain.hotel.dto.TimesaleHotelResponse;
import com.pser.hotel.domain.hotel.dto.TimesaleMapper;
import com.pser.hotel.domain.hotel.dto.TimesaleRoomResponse;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TimesaleDaoImpl implements TimesaleCustom {
    private final JPAQueryFactory queryFactory;
    private final TimesaleMapper timesaleMapper;
    private final HotelDao hotelDao;

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

    @Override
    public Slice<TimesaleHotelResponse> findNowTimesaleHotel(Pageable pageable) {
        QRoom qRoom = QRoom.room;
        QHotel qHotel = QHotel.hotel;
        QTimeSale qTimeSale = QTimeSale.timeSale;
        LocalDateTime now = LocalDateTime.now();

        List<Tuple> timesaleData = queryFactory
                .select(qTimeSale, qRoom, qHotel)
                .from(qTimeSale)
                .join(qTimeSale.room, qRoom)
                .join(qRoom.hotel, qHotel)
                .where(qTimeSale.startAt.before(now).and(qTimeSale.endAt.after(now)))
                .fetch();

        List<TimesaleHotelResponse> timesaleHotelResponses = timesaleData.stream()
                .map(tuple -> {
                    TimeSale timeSale = tuple.get(qTimeSale);
                    Room room = tuple.get(qRoom);
                    Hotel hotel = tuple.get(qHotel);

                    TimesaleRoomResponse roomResponse = timesaleMapper.changeToTimesaleRoomResponse(room,
                            Objects.requireNonNull(timeSale).getPrice());

                    TimesaleHotelResponse hotelResponse = timesaleMapper.changeToTimeHotelResponse(
                            hotel,
                            roomResponse.getPreviousPrice(),
                            roomResponse.getSalePrice()
                    );

                    double hotelGrade = hotelDao.getHotelGrade(Objects.requireNonNull(hotel).getId());
                    hotelResponse.setGradeAverage(hotelGrade);

                    return hotelResponse;
                })
                .toList();

        Map<String, TimesaleHotelResponse> hotelResponseMap = timesaleHotelResponses.stream()
                .collect(Collectors.toMap(
                        TimesaleHotelResponse::getName,
                        response -> response,
                        (existing, replacement) -> existing.getSalePrice() < replacement.getSalePrice() ? existing
                                : replacement
                ));

        List<TimesaleHotelResponse> filteredHotelResponses = new ArrayList<>(hotelResponseMap.values());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredHotelResponses.size());
        List<TimesaleHotelResponse> subList = filteredHotelResponses.subList(start, end);

        boolean hasNext = end < filteredHotelResponses.size();

        return new SliceImpl<>(subList, pageable, hasNext);
    }
}
