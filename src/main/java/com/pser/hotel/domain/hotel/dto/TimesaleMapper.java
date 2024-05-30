package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimesaleMapper {

    @Mapping(source = "timesaleCreateRequest.price", target = "price")
    TimeSale changeToTimesale(TimesaleCreateRequest timesaleCreateRequest, Room room);
}
