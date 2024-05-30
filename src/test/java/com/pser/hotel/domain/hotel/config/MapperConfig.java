package com.pser.hotel.domain.hotel.config;

import com.pser.hotel.domain.hotel.dto.mapper.HotelMapper;
import com.pser.hotel.domain.hotel.dto.mapper.HotelMapperImpl;
import com.pser.hotel.domain.hotel.dto.mapper.TimesaleMapper;
import com.pser.hotel.domain.hotel.dto.mapper.TimesaleMapperImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MapperConfig {
    @Bean
    public HotelMapper hotelMapper() {
        return new HotelMapperImpl();
    }

    @Bean
    public TimesaleMapper timesaleMapper() {
        return new TimesaleMapperImpl();
    }
}
