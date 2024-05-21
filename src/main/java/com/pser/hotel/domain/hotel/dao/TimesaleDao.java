package com.pser.hotel.domain.hotel.dao;

import com.pser.hotel.domain.hotel.domain.TimeSale;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimesaleDao extends JpaRepository<TimeSale, Long>, TimesaleCustom {
    List<TimeSale> findByRoomId(Long roomId);
}
