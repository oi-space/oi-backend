package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.FacilityDao;
import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.HotelImageDao;
import com.pser.hotel.domain.hotel.dao.UserDao;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelImage;
import com.pser.hotel.domain.hotel.dto.HotelCreateRequest;
import com.pser.hotel.domain.hotel.dto.HotelMapper;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.HotelSummaryResponse;
import com.pser.hotel.domain.hotel.dto.HotelUpdateRequest;
import com.pser.hotel.domain.member.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelDao hotelDao;
    private final FacilityDao facilityDao;
    private final UserDao userDao;
    private final HotelImageDao hotelImageDao;
    private final HotelMapper hotelMapper;

    public Slice<HotelSummaryResponse> getAllHotelData(Pageable pageable) {
        return hotelDao.findAllWithGradeAndPrice(pageable);
    }

    public HotelResponse getHotelDataById(Long id) {
        return hotelDao.findHotel(id);
    }

    public Slice<HotelSummaryResponse> searchHotelData(HotelSearchRequest hotelSearchRequest,
                                                       Pageable pageable) {
        return hotelDao.search(hotelSearchRequest, pageable);
    }

    @Transactional
    public Long saveHotelData(HotelCreateRequest hotelCreateRequest, Long userId) {
        User user = userDao.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found user id"));

        Hotel hotel = hotelMapper.changeToHotel(hotelCreateRequest, user);
        Facility facility = hotelMapper.changeToFacility(hotelCreateRequest, hotel);
        List<HotelImage> hotelImages = createHotelImages(hotel, hotelCreateRequest.getHotelImageUrls());

        hotelDao.save(hotel);

        return hotel.getId();
    }

    @Transactional
    public void updateHotelData(HotelUpdateRequest hotelUpdateRequest, Long hotelId) {
        Hotel hotel = hotelDao.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found hotel"));
        Facility facility = facilityDao.findByHotelId(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found facility"));

        List<HotelImage> hotelImages = hotelUpdateRequest.getHotelImageUrls().stream()
                .map(url -> hotelImageDao.findByImageUrl(url)
                        .orElseGet(() -> hotelImageDao.save(createImage(hotel, url))))
                .toList();

        hotel.getImages().clear();
        hotel.getImages().addAll(hotelImages);

        hotelMapper.updateHotelFromDto(hotelUpdateRequest, hotel);
        hotelMapper.updateFacilityFromDto(hotelUpdateRequest, facility);
    }

    @Transactional
    public void deleteHotelData(Long hotelId) {
        Hotel hotel = hotelDao.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found hotel"));

        hotelDao.delete(hotel);
    }

    private HotelImage createImage(Hotel hotel, String hotelImg) {
        return HotelImage.builder()
                .imageUrl(hotelImg)
                .hotel(hotel)
                .build();
    }

    private List<HotelImage> createHotelImages(Hotel hotel, List<String> hotelImgs) {
        return hotelImgs.stream()
                .map(url -> createImage(hotel, url))
                .collect(Collectors.toList());
    }
}
