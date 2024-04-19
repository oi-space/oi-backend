package com.pser.hotel.domain.hotel.dao;

import static com.pser.hotel.domain.hotel.domain.QRoom.room;

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
        List<RoomResponseDto> fetch = queryFactory.select(
                        new QRoomResponseDto(
                                room.name,
                                room.description,
                                room.precaution,
                                room.price,
                                room.checkIn,
                                room.checkOut,
                                room.standardCapacity,
                                room.maxCapacity,
                                room.totalRooms,
                                room.amenity
                        )
                )
                .from(room)
                .where(
                        searchCondition(request),
                        amenityCondition(request),
                        priceCondition(request),
                        standardCapacityCondition(request),
                        maxCapacityCondition(request)
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
                        searchCondition(request),
                        amenityCondition(request),
                        priceCondition(request),
                        standardCapacityCondition(request),
                        maxCapacityCondition(request)
                )
                .fetchOne();
        if (count == null) {
            count = 0L;
        }
        return count;
    }

    private BooleanBuilder maxCapacityCondition(RoomSearchRequest request) {
        return new BooleanBuilder()
                .and(maxCapacityEq(request.getMaxCapacity()))
                .and(maxCapacityGte(request.getMaxCapacityGte()))
                .and(maxCapacityLte(request.getMaxCapacityLte()));
    }

    private BooleanBuilder standardCapacityCondition(RoomSearchRequest request) {
        return new BooleanBuilder()
                .and(standardCapacityEq(request.getStandardCapacity()))
                .and(standardCapacityGte(request.getStandardCapacityGte()))
                .and(standardCapacityLte(request.getStandardCapacityLte()));
    }

    private BooleanBuilder priceCondition(RoomSearchRequest request) {
        return new BooleanBuilder()
                .and(priceEq(request.getPrice()))
                .and(priceGte(request.getPriceGte()))
                .and(priceLte(request.getPriceLte()));
    }

    private BooleanBuilder searchCondition(RoomSearchRequest request) {
        return new BooleanBuilder()
                .and(keywordContains(request.getKeyword()));
    }

    private BooleanBuilder amenityCondition(RoomSearchRequest request) {
        return new BooleanBuilder()
                .and(heatingIsTrue(request.getHeatingSystem()))
                .and(tvIsTrue(request.getTv()))
                .and(refrigeratorIsTrue(request.getRefrigerator()))
                .and(airConditionerIsTrue(request.getAirConditioner()))
                .and(washerIsTrue(request.getWasher()))
                .and(terraceIsTrue(request.getTerrace()))
                .and(coffeeMachineIsTrue(request.getCoffeeMachine()))
                .and(internetIsTrue(request.getInternet()))
                .and(kitchenIsTrue(request.getKitchen()))
                .and(bathtubIsTrue(request.getBathtub()))
                .and(ironIsTrue(request.getIron()))
                .and(poolIsTrue(request.getPool()))
                .and(petIsTrue(request.getPet()))
                .and(innAnnexIsTrue(request.getInAnnex()));
    }

    private BooleanExpression maxCapacityEq(Integer maxCapacity) {
        return maxCapacity != null ? room.maxCapacity.eq(maxCapacity) : null;
    }

    private BooleanExpression maxCapacityGte(Integer maxCapacityGte) {
        return maxCapacityGte != null ? room.maxCapacity.goe(maxCapacityGte) : null;
    }

    private BooleanExpression maxCapacityLte(Integer maxCapacityLte) {
        return maxCapacityLte != null ? room.maxCapacity.loe(maxCapacityLte) : null;
    }

    private BooleanExpression standardCapacityEq(Integer standardCapacity) {
        return standardCapacity != null ? room.standardCapacity.eq(standardCapacity) : null;
    }

    private BooleanExpression standardCapacityGte(Integer standardCapacityGte) {
        return standardCapacityGte != null ? room.standardCapacity.goe(standardCapacityGte) : null;
    }

    private BooleanExpression standardCapacityLte(Integer standardCapacityLte) {
        return standardCapacityLte != null ? room.standardCapacity.loe(standardCapacityLte) : null;
    }

    private BooleanExpression priceEq(Integer price) {
        return price != null ? room.price.eq(price) : null;
    }

    private BooleanExpression priceGte(Integer priceGte) {
        return priceGte != null ? room.price.goe(priceGte) : null;
    }

    private BooleanExpression priceLte(Integer priceLte) {
        return priceLte != null ? room.price.loe(priceLte) : null;
    }

    private BooleanExpression heatingIsTrue(Boolean heatingSystem) {
        return heatingSystem != null ? room.amenity.heatingSystem.isTrue() : null;
    }

    private BooleanExpression tvIsTrue(Boolean tv) {
        return tv != null ? room.amenity.tv.isTrue() : null;
    }

    private BooleanExpression refrigeratorIsTrue(Boolean refrigerator) {
        return refrigerator != null ? room.amenity.refrigerator.isTrue() : null;
    }

    private BooleanExpression airConditionerIsTrue(Boolean airConditioner) {
        return airConditioner != null ? room.amenity.airConditioner.isTrue() : null;
    }

    private BooleanExpression washerIsTrue(Boolean washer) {
        return washer != null ? room.amenity.washer.isTrue() : null;
    }

    private BooleanExpression terraceIsTrue(Boolean terrace) {
        return terrace != null ? room.amenity.terrace.isTrue() : null;
    }

    private BooleanExpression coffeeMachineIsTrue(Boolean coffeeMachine) {
        return coffeeMachine != null ? room.amenity.coffeeMachine.isTrue() : null;
    }

    private BooleanExpression internetIsTrue(Boolean internet) {
        return internet != null ? room.amenity.internet.isTrue() : null;
    }

    private BooleanExpression kitchenIsTrue(Boolean kitchen) {
        return kitchen != null ? room.amenity.kitchen.isTrue() : null;
    }

    private BooleanExpression bathtubIsTrue(Boolean bathtub) {
        return bathtub != null ? room.amenity.bathtub.isTrue() : null;
    }

    private BooleanExpression ironIsTrue(Boolean iron) {
        return iron != null ? room.amenity.iron.isTrue() : null;
    }

    private BooleanExpression poolIsTrue(Boolean pool) {
        return pool != null ? room.amenity.pool.isTrue() : null;
    }

    private BooleanExpression petIsTrue(Boolean pet) {
        return pet != null ? room.amenity.pet.isTrue() : null;
    }

    private BooleanExpression innAnnexIsTrue(Boolean innAnnex) {
        return innAnnex != null ? room.amenity.inAnnex.isTrue() : null;
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
