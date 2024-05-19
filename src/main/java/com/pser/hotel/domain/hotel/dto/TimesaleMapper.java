package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimesaleMapper {

    @Mapping(source = "timesaleCreateRequest.price", target = "price")
    TimeSale changeToTimesale(TimesaleCreateRequest timesaleCreateRequest, Room room);

    @Mapping(source = "room.price", target = "previousPrice")
    @Mapping(source = "price", target = "salePrice")
    @Mapping(source = "room.hotel.id", target = "hotelId")
    TimesaleRoomResponse changeToTimesaleRoomResponse(Room room, int price);

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
    TimesaleHotelResponse changeToTimeHotelResponse(Hotel hotel, int previousPrice, int salePrice);
}
