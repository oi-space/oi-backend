package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.TimeSale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimesaleDao extends JpaRepository<TimeSale, Long>, TimesaleCustom {
}
