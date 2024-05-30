package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.QHotel;
import com.pser.hotel.domain.hotel.domain.QRoom;
import com.pser.hotel.domain.hotel.domain.QTimeSale;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import com.pser.hotel.domain.hotel.dto.mapper.HotelMapper;
import com.pser.hotel.domain.hotel.dto.response.HotelSummaryResponse;
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
    private final HotelDao hotelDao;
    private final HotelMapper hotelMapper;

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
    public Slice<HotelSummaryResponse> findNowTimesaleHotel(Pageable pageable) {
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

        List<HotelSummaryResponse> timesaleHotelResponses = timesaleData.stream()
                .map(tuple -> {
                    TimeSale timeSale = tuple.get(qTimeSale);
                    Room room = tuple.get(qRoom);
                    Hotel hotel = tuple.get(qHotel);

                    int salePrice = Objects.requireNonNull(timeSale).getPrice();
                    int previousPrice = Objects.requireNonNull(room).getPrice();
                    double hotelGrade = hotelDao.getHotelGrade(Objects.requireNonNull(hotel).getId());

                    HotelSummaryResponse summaryResponse = hotelMapper.changeToHotelSummaryResponse(hotel, hotelGrade,
                            salePrice, previousPrice);

                    return summaryResponse;

                })
                .toList();

        Map<String, HotelSummaryResponse> hotelResponseMap = timesaleHotelResponses.stream()
                .collect(Collectors.toMap(
                        HotelSummaryResponse::getName,
                        response -> response,
                        (existing, replacement) -> existing.getSalePrice() < replacement.getSalePrice() ? existing
                                : replacement
                ));

        List<HotelSummaryResponse> filteredHotelResponses = new ArrayList<>(hotelResponseMap.values());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredHotelResponses.size());
        List<HotelSummaryResponse> subList = filteredHotelResponses.subList(start, end);

        boolean hasNext = end < filteredHotelResponses.size();

        return new SliceImpl<>(subList, pageable, hasNext);
    }
}
