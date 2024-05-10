package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.member.domain.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = ComponentModel.SPRING)
public interface HotelMapper {
    @Mapping(target = "parkingLot", source = "hotel.facility.parkingLot")
    @Mapping(target = "wifi", source = "hotel.facility.wifi")
    @Mapping(target = "barbecue", source = "hotel.facility.barbecue")
    @Mapping(target = "sauna", source = "hotel.facility.sauna")
    @Mapping(target = "swimmingPool", source = "hotel.facility.swimmingPool")
    @Mapping(target = "restaurant", source = "hotel.facility.restaurant")
    @Mapping(target = "roofTop", source = "hotel.facility.roofTop")
    @Mapping(target = "fitness", source = "hotel.facility.fitness")
    @Mapping(target = "dryer", source = "hotel.facility.dryer")
    @Mapping(target = "breakfast", source = "hotel.facility.breakfast")
    @Mapping(target = "smokingArea", source = "hotel.facility.smokingArea")
    @Mapping(target = "allTimeDesk", source = "hotel.facility.allTimeDesk")
    @Mapping(target = "luggageStorage", source = "hotel.facility.luggageStorage")
    @Mapping(target = "snackBar", source = "hotel.facility.snackBar")
    @Mapping(target = "petFriendly", source = "hotel.facility.petFriendly")
    @Mapping(target = "userId", source = "user.id")
    HotelResponse changeToHotelResponse(Hotel hotel);

    Hotel changeToHotel(HotelCreateRequest hotelCreateRequest, User user);

    Facility changeToFacility(HotelCreateRequest hotelCreateRequest, Hotel hotel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateHotelFromDto(HotelUpdateRequest hotelUpdateRequest, @MappingTarget Hotel hotel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateFacilityFromDto(HotelUpdateRequest hotelUpdateRequest, @MappingTarget Facility facility);
}
