package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.HotelCategoryConverter;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.QHotel;
import com.pser.hotel.domain.hotel.domain.QHotelImage;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.QHotelResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HotelDaoImpl implements HotelDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<HotelResponse> search(HotelSearchRequest hotelSearchRequest, Pageable pageable) {
        List<HotelResponse> content = queryFactory
                .select(new QHotelResponse(
                        QHotel.hotel.id,
                        QHotel.hotel.user.id,
                        QHotel.hotel.name,
                        QHotel.hotel.category,
                        QHotel.hotel.description,
                        QHotel.hotel.notice,
                        QHotel.hotel.province,
                        QHotel.hotel.city,
                        QHotel.hotel.district,
                        QHotel.hotel.detailedAddress,
                        QHotel.hotel.latitude,
                        QHotel.hotel.longtitude,
                        QHotel.hotel.mainImage,
                        QHotel.hotel.businessNumber,
                        QHotel.hotel.certUrl,
                        QHotel.hotel.visitGuidance,
                        QHotel.hotel.facility.parkingLot,
                        QHotel.hotel.facility.wifi,
                        QHotel.hotel.facility.barbecue,
                        QHotel.hotel.facility.sauna,
                        QHotel.hotel.facility.swimmingPool,
                        QHotel.hotel.facility.restaurant,
                        QHotel.hotel.facility.roofTop,
                        QHotel.hotel.facility.fitness,
                        QHotel.hotel.facility.dryer,
                        QHotel.hotel.facility.breakfast,
                        QHotel.hotel.facility.smokingArea,
                        QHotel.hotel.facility.allTimeDesk,
                        QHotel.hotel.facility.luggageStorage,
                        QHotel.hotel.facility.snackBar,
                        QHotel.hotel.facility.petFriendly
                ))
                .from(QHotel.hotel)
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
                        containsKeywordPredicate(hotelSearchRequest.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        content.forEach(hotelResponse -> {
            List<String> images = queryFactory
                    .select(QHotelImage.hotelImage.imageUrl)
                    .from(QHotelImage.hotelImage)
                    .where(QHotelImage.hotelImage.hotel.id.eq(hotelResponse.getId()))
                    .fetch();
            hotelResponse.setHotelImageUrls(images);
        });

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
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