package com.pser.hotel.domain.hotel.dao;

import static com.pser.hotel.domain.hotel.domain.QRoom.room;

import com.pser.hotel.domain.hotel.domain.QRoom;
import com.pser.hotel.domain.hotel.dto.QRoomResponseDto;
import com.pser.hotel.domain.hotel.dto.RoomResponseDto;
import com.pser.hotel.domain.hotel.dto.RoomSearchRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomDaoImpl implements RoomDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RoomResponseDto> search(RoomSearchRequest request, Pageable pageable) {
        QRoom room = QRoom.room;
        List<RoomResponseDto> fetch = queryFactory.select(
                        new QRoomResponseDto(
                                room.name
                        )
                )
                .from(room)
                .where(
                        searchCondition(request)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long count = searchForCount(request);
        return new PageImpl<RoomResponseDto>(fetch, pageable, count);
    }

    public Long searchForCount(RoomSearchRequest request) {
        Long count = queryFactory
                .select(room.count())
                .from(room)
                .where(
                        searchCondition(request)
                )
                .fetchOne();
        if (count == null) {
            count = 0L;
        }
        return count;
    }

    private BooleanBuilder searchCondition(RoomSearchRequest request) {
        return new BooleanBuilder()
                .and(keywordContains(request.getKeyword()));
    }

    private BooleanBuilder keywordContains(String keyword) {
        return new BooleanBuilder()
                .or(nameContains(keyword))
                .or(descriptionContains(keyword))
                .or(precautionContains(keyword));
    }

    private BooleanExpression nameContains(String keyword) {
        return keyword != null ? room.name.contains(keyword) : null;
    }

    private BooleanExpression descriptionContains(String keyword) {
        return keyword != null ? room.description.contains(keyword) : null;
    }

    private BooleanExpression precautionContains(String keyword) {
        return keyword != null ? room.precaution.contains(keyword) : null;
    }
}
