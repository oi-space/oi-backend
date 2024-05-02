package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.HotelCategoryConverter;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.domain.QHotel;
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
    public Slice<HotelResponse> search(HotelSearchRequest hotelSearchRequest, Pageable pageable){
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
                QHotel.hotel.facility.barbecue
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
                containsKeywordPredicate(hotelSearchRequest.getKeyword())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

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

    private Predicate getCategoryPredicate(HotelCategoryEnum categoryEnum) {
        HotelCategoryConverter hotelCategoryConverter = new HotelCategoryConverter();
        return categoryEnum != null ? QHotel.hotel.category.stringValue()
            .eq(hotelCategoryConverter.convertToDatabaseColumn(categoryEnum).toString()) : null;
    }

    private Predicate containsKeywordPredicate(String keyword) {
        BooleanBuilder builder = new BooleanBuilder();

        if(StringUtils.hasText(keyword)){
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