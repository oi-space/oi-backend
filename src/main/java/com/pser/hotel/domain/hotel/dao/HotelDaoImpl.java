package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryConverter;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.QHotel;
import com.pser.hotel.domain.hotel.domain.QHotelImage;
import com.pser.hotel.domain.hotel.domain.QReservation;
import com.pser.hotel.domain.hotel.domain.QReview;
import com.pser.hotel.domain.hotel.domain.QRoom;
import com.pser.hotel.domain.hotel.domain.QTimeSale;
import com.pser.hotel.domain.hotel.dto.HotelMapper;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.HotelSummaryResponse;
import com.pser.hotel.domain.hotel.dto.QHotelSummaryResponse;
import com.pser.hotel.domain.model.GradeEnum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;

@Repository
@RequiredArgsConstructor
public class HotelDaoImpl implements HotelDaoCustom {
    private final JPAQueryFactory queryFactory;
    private final HotelMapper hotelMapper;

    @Override
    public Slice<HotelSummaryResponse> search(HotelSearchRequest hotelSearchRequest, Pageable pageable) {
        List<HotelSummaryResponse> content = queryFactory
                .select(new QHotelSummaryResponse(
                        QHotel.hotel.id,
                        QHotel.hotel.name,
                        QHotel.hotel.category,
                        QHotel.hotel.description,
                        QHotel.hotel.mainImage
                ))
                .from(QHotel.hotel)
                .leftJoin(QRoom.room).on(QRoom.room.hotel.id.eq(QHotel.hotel.id))
                .where(
                        getNamePredicate(hotelSearchRequest.getName()),
                        getProvincePredicate(hotelSearchRequest.getProvince()),
                        getCityPredicate(hotelSearchRequest.getCity()),
                        getDistrictPredicate(hotelSearchRequest.getDistrict()),
                        getDetailedAddressPredicate(hotelSearchRequest.getDetailedAddress()),
                        getBarbecuePredicate(hotelSearchRequest.getBarbecue()),
                        getWifiPredicate(hotelSearchRequest.getWifi()),
                        getParkingLotPredicate(hotelSearchRequest.getParkingLot()),
                        getCategoryPredicate(hotelSearchRequest.getCategory()),
                        getSaunaPredicate(hotelSearchRequest.getSauna()),
                        getSwimmingPoolPredicate(hotelSearchRequest.getSwimmingPool()),
                        getRestaurantPredicate(hotelSearchRequest.getRestaurant()),
                        getRoofTopPredicate(hotelSearchRequest.getRoofTop()),
                        getFitnessPredicate(hotelSearchRequest.getFitness()),
                        getDryerPredicate(hotelSearchRequest.getDryer()),
                        getBreakfistPredicate(hotelSearchRequest.getBreakfast()),
                        getSmokingAreaPredicate(hotelSearchRequest.getSmokingArea()),
                        getAllTimeDeskPredicate(hotelSearchRequest.getAllTimeDesk()),
                        getLuggageStoragePredicate(hotelSearchRequest.getLuggageStorage()),
                        getSnackBarPredicate(hotelSearchRequest.getSnackBar()),
                        getPetFriendlyPredicate(hotelSearchRequest.getPetFriendly()),
                        getReservationBetweenPredicate(hotelSearchRequest.getSearchStartAt(),
                                // 숙박 시작일 ~ 숙박 종료일 동안 예약이 가능한 객실을 보유한 호텔만 리스트에 담는다
                                hotelSearchRequest.getSearchEndAt()),
                        containsKeywordPredicate(hotelSearchRequest.getKeyword()),
                        getPeoplePredicate(hotelSearchRequest.getPeople()) // 인원보다 많은 인원을 수용할 수 있는 객실을 보유한 호텔만 리스트에 담는다
                ).groupBy(QHotel.hotel.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        content = content.stream().map(hotelSummaryResponse -> {
            List<String> images = queryFactory
                    .select(QHotelImage.hotelImage.imageUrl)
                    .from(QHotelImage.hotelImage)
                    .where(QHotelImage.hotelImage.hotel.id.eq(hotelSummaryResponse.getId()))
                    .fetch();
            hotelSummaryResponse.setHotelImageUrls(images);
            hotelSummaryResponse.setGradeAverage(getHotelGrade(hotelSummaryResponse.getId()));
            int previousPrice = getPreviousPrice(hotelSummaryResponse.getId());
            hotelSummaryResponse.setPreviousPrice(previousPrice);
            hotelSummaryResponse.setSalePrice(getSalePrice(hotelSummaryResponse.getId(), previousPrice));
            return hotelSummaryResponse;
        }).collect(Collectors.toList());

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
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
    public Slice<HotelSummaryResponse> findAllWithGradeAndPrice(Pageable pageable) {
        QHotel qHotel = QHotel.hotel;

        List<Hotel> hotels = queryFactory.selectFrom(qHotel)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<HotelSummaryResponse> hotelResponses = hotels.stream()
                .map(hotel -> {
                    double hotelGrade = getHotelGrade(hotel.getId());
                    int previousPrice = getPreviousPrice(hotel.getId());
                    int salePrice = getSalePrice(hotel.getId(), previousPrice);
                    return hotelMapper.changeToHotelSummaryResponse(hotel, hotelGrade, salePrice, previousPrice);
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

    private Predicate getReservationBetweenPredicate(LocalDate searchStartAt, LocalDate searchEndAt) {
        if (searchStartAt == null || searchEndAt == null) {
            return null;
        }

        QRoom qRoom = QRoom.room;
        QReservation qReservation = QReservation.reservation;

        return qRoom.id.notIn(
                JPAExpressions.select(qReservation.room.id)
                        .from(qReservation)
                        .where(qReservation.startAt.loe(searchEndAt)
                                .and(qReservation.endAt.goe(searchStartAt)))
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