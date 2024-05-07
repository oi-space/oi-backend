package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dto.HotelCreateRequest;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.HotelUpdateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelService {
    public Slice<HotelResponse> getAllHotelData(Pageable pageable) {
        List<HotelResponse> data = new ArrayList<>();
        data.add(new HotelResponse());
        return new SliceImpl<>(data, pageable, true);
    }
    public Optional<HotelResponse> getHotelDataById(Long id) {
        HotelResponse data = new HotelResponse();
        return Optional.of(data);
    }
    public Slice<HotelResponse> searchHotelData(HotelSearchRequest hotelSearchRequest, Pageable pageable) {
        List<HotelResponse> data = new ArrayList<>();
        data.add(new HotelResponse());
        return new SliceImpl<>(data, pageable, true);
    }
    public Long saveHotelData(HotelCreateRequest hotelCreateRequest, Long userId) {
        return 1L;
    }
    public Long updateHotelData(HotelUpdateRequest hotelUpdateRequest, Long hotelId, Long userId){
        return 1L;
    }
    public Long deleteHotelData(Long hotelId, Long userId) {
        return 1L;
    }
}
