package com.pser.hotel.domain.hotel.dao;

import static com.pser.hotel.domain.hotel.domain.QHotel.hotel;
import static com.pser.hotel.domain.hotel.domain.QReservation.reservation;
import static com.pser.hotel.domain.hotel.domain.QRoom.room;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryConverter;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.QHotel;
import com.pser.hotel.domain.hotel.domain.QReservation;
import com.pser.hotel.domain.hotel.domain.QReview;
import com.pser.hotel.domain.hotel.domain.QRoom;
import com.pser.hotel.domain.hotel.domain.QTimeSale;
import com.pser.hotel.domain.hotel.dto.HotelMapper;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.QHotelResponse;
import com.pser.hotel.domain.model.GradeEnum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Repository
@RequiredArgsConstructor
public class HotelDaoImpl implements HotelDaoCustom {

    private final JPAQueryFactory queryFactory;

    private final HotelMapper hotelMapper;


    @Override
    public Slice<HotelResponse> search(HotelSearchRequest request, Pageable pageable) {

        List<HotelResponse> fetch = queryFactory.select(
                        new QHotelResponse(
                                hotel.id,
                                hotel.name,
                                hotel.description,
                                hotel.city,
                                hotel.district,
                                hotel.detailedAddress,
                                hotel.mainImage,
                                hotel.visitGuidance,
                                hotel.createdAt
                        ))
                .from(hotel)
                .where(
                        cursorCreateDateAndCursorId(request.getNextCursorCreatedAt(), request.getNextCursorId()),

                        provinceEq(request.getKeyword()),
                        getBarbecuePredicate(request.getBarbecue()),
                        getWifiPredicate(request.getWifi()),
                        getParkingLotPredicate(request.getParkingLot()),
                        getCategoryPredicate(request.getCategory()),
                        getSaunaPredicate(request.getSauna()),
                        getSwimmingPoolPredicate(request.getSwimmingPool()),
                        getRestaurantPredicate(request.getRestaurant()),
                        getRoofTopPredicate(request.getRoofTop()),
                        getFitnessPredicate(request.getFitness()),
                        getDryerPredicate(request.getDryer()),
                        getBreakfistPredicate(request.getBreakfast()),
                        getSmokingAreaPredicate(request.getSmokingArea()),
                        getAllTimeDeskPredicate(request.getAllTimeDesk()),
                        getLuggageStoragePredicate(request.getLuggageStorage()),
                        getSnackBarPredicate(request.getSnackBar()),
                        getPetFriendlyPredicate(request.getPetFriendly()),
                        hotelIdIn(request)
                )
                .orderBy(hotel.createdAt.desc())
                .limit(pageable.getPageSize())
                .fetch();

        long count = searchForCount(request);

        return new PageImpl<>(fetch, pageable, count);
    }

    private BooleanBuilder cursorCreateDateAndCursorId(LocalDateTime createdAt, Long cursorId) {
        return new BooleanBuilder()
                .and(createdAtEqAndIdLt(createdAt, cursorId))
                .or(createDateLt(createdAt));
    }

    private BooleanExpression createdAtEqAndIdLt(LocalDateTime createDate, Long cursorId) {
        if (createDate == null | cursorId == null) {
            return null;
        }
        return hotel.createdAt.eq(createDate)
                .and(hotel.id.lt(cursorId));
    }

    private BooleanExpression createDateLt(LocalDateTime createDate) {
        return createDate != null ? hotel.createdAt.lt(createDate) : null;

    }

    private long searchForCount(HotelSearchRequest request) {
        Long count = queryFactory
                .select(hotel.count())
                .from(hotel)
                .where(
                        provinceEq(request.getKeyword()),
                        getBarbecuePredicate(request.getBarbecue()),
                        getWifiPredicate(request.getWifi()),
                        getParkingLotPredicate(request.getParkingLot()),
                        getCategoryPredicate(request.getCategory()),
                        getSaunaPredicate(request.getSauna()),
                        getSwimmingPoolPredicate(request.getSwimmingPool()),
                        getRestaurantPredicate(request.getRestaurant()),
                        getRoofTopPredicate(request.getRoofTop()),
                        getFitnessPredicate(request.getFitness()),
                        getDryerPredicate(request.getDryer()),
                        getBreakfistPredicate(request.getBreakfast()),
                        getSmokingAreaPredicate(request.getSmokingArea()),
                        getAllTimeDeskPredicate(request.getAllTimeDesk()),
                        getLuggageStoragePredicate(request.getLuggageStorage()),
                        getSnackBarPredicate(request.getSnackBar()),
                        getPetFriendlyPredicate(request.getPetFriendly()),
                        hotelIdIn(request)
                ).fetchOne();

        if (count == null) {
            count = 0L;
        }
        return count;
    }


    private BooleanBuilder searchCondition(HotelSearchRequest request) {
        return new BooleanBuilder()
                .and(maxCapacityGt(request.getPeople()))
                .and(searchDateBetween(request.getSearchStartAt(), request.getSearchEndAt()));
    }

    private BooleanExpression searchDateBetween(LocalDate startAt, LocalDate endAt) {
        return startAt != null && endAt != null ?
                reservation.endAt.loe(startAt)
                        .or(reservation.startAt.gt(endAt))
                : null;
    }

    private BooleanExpression maxCapacityGt(Integer requestMaxCapacity) {
        return requestMaxCapacity != null ? room.maxCapacity.gt(requestMaxCapacity) : null;
    }

    private BooleanExpression provinceEq(String province) {
        return province != null ? hotel.province.eq(province) : null;
    }

    @Override
    public double getHotelGrade(Long hotelId) {

        List<GradeEnum> gradeEnumList = queryFactory.select(QReview.review.grade)
                .from(QReview.review)
                .join(QReview.review.reservation, QReservation.reservation)
                .join(QReservation.reservation.room, QRoom.room)
                .join(QRoom.room.hotel, QHotel.hotel)
                .where(QHotel.hotel.id.eq(hotelId))
                .fetch();

        return calculateGradeEnum(gradeEnumList);
    }

    @Override
    public Slice<HotelResponse> findAllWithGradeAndPrice(Pageable pageable) {
        QHotel qHotel = QHotel.hotel;

        List<Hotel> hotels = queryFactory.selectFrom(qHotel)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<HotelResponse> hotelResponses = hotels.stream()
                .map(hotel -> {
                    double hotelGrade = getHotelGrade(hotel.getId());
                    int previousPrice = getPreviousPrice(hotel.getId());
                    int salePrice = getSalePrice(hotel.getId(), previousPrice);
                    return hotelMapper.changeToHotelResponse(hotel, hotelGrade, salePrice, previousPrice);
                })
                .collect(Collectors.toList());

        boolean hasNext = hotelResponses.size() > pageable.getPageSize();
        if (hasNext) {
            hotelResponses.remove(hotelResponses.size() - 1);
        }

        return new SliceImpl<>(hotelResponses, pageable, hasNext);
    }

    @Override
    public HotelResponse findHotel(Long hotelId) {
        QHotel qHotel = QHotel.hotel;

        Hotel hotel = queryFactory.selectFrom(qHotel)
                .where(qHotel.id.eq(hotelId))
                .fetchOne();

        if (hotel == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found");
        }

        double hotelGrade = getHotelGrade(hotelId);
        int previousPrice = getPreviousPrice(hotelId);
        int salePrice = getSalePrice(hotelId, previousPrice);

        return hotelMapper.changeToHotelResponse(hotel, hotelGrade, salePrice, previousPrice);

    }

    private int getSalePrice(Long hotelId, int previousPrice) {
        QTimeSale qTimeSale = QTimeSale.timeSale;
        QRoom qRoom = QRoom.room;
        LocalDateTime now = LocalDateTime.now();

        Integer salePrice = queryFactory.select(qTimeSale.price.min())
                .from(qTimeSale)
                .join(qTimeSale.room, qRoom)
                .where(qRoom.hotel.id.eq(hotelId))
                .where(qTimeSale.startAt.before(now).and(qTimeSale.endAt.after(now)))
                .fetchOne();

        if (salePrice == null || previousPrice < salePrice) {
            salePrice = previousPrice;
        }

        return salePrice;
    }

    private int getPreviousPrice(Long hotelId) {
        QRoom qRoom = QRoom.room;

        Integer previousPrice = queryFactory.select(qRoom.price.min())
                .from(qRoom)
                .where(qRoom.hotel.id.eq(hotelId))
                .fetchOne();

        return previousPrice != null ? previousPrice : 0;
    }

    private double calculateGradeEnum(List<GradeEnum> gradeEnumList) {
        if (gradeEnumList.isEmpty()) {
            return 0.0;
        }
        return gradeEnumList.stream()
                .mapToInt(GradeEnum::getValue)
                .average()
                .orElse(0.0);
    }

    private Predicate getNamePredicate(String name) {
        return StringUtils.hasText(name) ? QHotel.hotel.name.contains(name) : null;
    }

    private Predicate getProvincePredicate(String province) {
        return StringUtils.hasText(province) ? QHotel.hotel.province.contains(province) : null;
    }

    private Predicate getCityPredicate(String city) {
        return StringUtils.hasText(city) ? QHotel.hotel.city.contains(city) : null;
    }

    private Predicate getDistrictPredicate(String district) {
        return StringUtils.hasText(district) ? QHotel.hotel.district.contains(district) : null;
    }

    private Predicate getDetailedAddressPredicate(String detailedAddress) {
        return StringUtils.hasText(detailedAddress) ? QHotel.hotel.detailedAddress.contains(detailedAddress) : null;
    }

    private Predicate getBarbecuePredicate(Boolean barbecue) {
        return barbecue != null ? QHotel.hotel.facility.barbecue.eq(barbecue) : null;
    }

    private Predicate getWifiPredicate(Boolean wifi) {
        return wifi != null ? QHotel.hotel.facility.wifi.eq(wifi) : null;
    }

    private Predicate getParkingLotPredicate(Boolean parkingLot) {
        return parkingLot != null ? QHotel.hotel.facility.parkingLot.eq(parkingLot) : null;
    }

    private Predicate getSaunaPredicate(Boolean sauna) {
        return sauna != null ? QHotel.hotel.facility.sauna.eq(sauna) : null;
    }

    private Predicate getSwimmingPoolPredicate(Boolean swimmingPool) {
        return swimmingPool != null ? QHotel.hotel.facility.swimmingPool.eq(swimmingPool) : null;
    }

    private Predicate getRestaurantPredicate(Boolean restaurant) {
        return restaurant != null ? QHotel.hotel.facility.restaurant.eq(restaurant) : null;
    }

    private Predicate getRoofTopPredicate(Boolean roofTop) {
        return roofTop != null ? QHotel.hotel.facility.roofTop.eq(roofTop) : null;
    }

    private Predicate getFitnessPredicate(Boolean fitness) {
        return fitness != null ? QHotel.hotel.facility.fitness.eq(fitness) : null;
    }

    private Predicate getDryerPredicate(Boolean dryer) {
        return dryer != null ? QHotel.hotel.facility.dryer.eq(dryer) : null;
    }

    private Predicate getBreakfistPredicate(Boolean breakfast) {
        return breakfast != null ? QHotel.hotel.facility.breakfast.eq(breakfast) : null;
    }

    private Predicate getSmokingAreaPredicate(Boolean smokingArea) {
        return smokingArea != null ? QHotel.hotel.facility.smokingArea.eq(smokingArea) : null;
    }

    private Predicate getAllTimeDeskPredicate(Boolean allTimeDesk) {
        return allTimeDesk != null ? QHotel.hotel.facility.allTimeDesk.eq(allTimeDesk) : null;
    }

    private Predicate getLuggageStoragePredicate(Boolean luggageStorage) {
        return luggageStorage != null ? QHotel.hotel.facility.luggageStorage.eq(luggageStorage) : null;
    }

    private Predicate getSnackBarPredicate(Boolean snackBar) {
        return snackBar != null ? QHotel.hotel.facility.snackBar.eq(snackBar) : null;
    }

    private Predicate getPetFriendlyPredicate(Boolean petFriendly) {
        return petFriendly != null ? QHotel.hotel.facility.petFriendly.eq(petFriendly) : null;
    }

    private Predicate getCategoryPredicate(HotelCategoryEnum categoryEnum) {
        HotelCategoryConverter hotelCategoryConverter = new HotelCategoryConverter();
        return categoryEnum != null ? QHotel.hotel.category.stringValue()
                .eq(hotelCategoryConverter.convertToDatabaseColumn(categoryEnum).toString()) : null;
    }

    private Predicate getPeoplePredicate(Integer people) {
        return people != null ? QRoom.room.maxCapacity.goe(people) : null;
    }

    private Predicate hotelIdIn(HotelSearchRequest request) {
        if (request.getPeople() == null && (request.getSearchStartAt() == null || request.getSearchEndAt() == null)) {
            return null;
        } else if (request.getSearchStartAt() == null || request.getSearchEndAt() == null) {
            return hotel.id.in(
                    JPAExpressions.select(room.hotel.id)
                            .from(room)
                            .where(
                                    maxCapacityGt(request.getPeople())
                            )
                            .distinct()
            );
        }
        return hotel.id.in(
                JPAExpressions.select(room.hotel.id)
                        .from(room)
                        .innerJoin(reservation)
                        .on(room.id.eq(reservation.room.id))
                        .where(
                                searchCondition(request)
                        ).distinct()
        );
    }

    private Predicate containsKeywordPredicate(String keyword) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(keyword)) {
            QHotel hotel = QHotel.hotel;
            builder.or(hotel.name.contains(keyword));
            builder.or(hotel.province.contains(keyword));
            builder.or(hotel.city.contains(keyword));
            builder.or(hotel.district.contains(keyword));
            builder.or(hotel.detailedAddress.contains(keyword));
        }
        return builder.getValue();

    }
}