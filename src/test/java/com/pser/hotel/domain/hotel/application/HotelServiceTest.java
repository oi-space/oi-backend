package com.pser.hotel.domain.hotel.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.pser.hotel.domain.hotel.dao.FacilityDao;
import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.UserDao;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.dto.HotelCreateRequest;
import com.pser.hotel.domain.hotel.dto.HotelMapper;
import com.pser.hotel.domain.hotel.dto.HotelResponse;
import com.pser.hotel.domain.hotel.dto.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.HotelUpdateRequest;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @Mock
    HotelDao hotelDao;
    @Mock
    FacilityDao facilityDao;
    @Mock
    UserDao userDao;
    @Mock
    HotelMapper hotelMapper;
    MockMvc mockMvc;
    User user;
    Hotel hotel;
    Facility facility;
    PageRequest pageRequest;
    HotelSearchRequest hotelSearchRequest;
    HotelCreateRequest hotelCreateRequest;
    HotelUpdateRequest hotelUpdateRequest;

    @BeforeEach
    public void setUp() {
        pageRequest = PageRequest.of(0, 10);
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        facility = Utils.createFacility(hotel);
        MockitoAnnotations.openMocks(this);
        hotelSearchRequest = createSearchRequest();
        hotelCreateRequest = createCreateRequest(hotel);
        hotelUpdateRequest = createUpdateRequest(hotel);
    }
    @Test
    @DisplayName("숙소 전체 조회 Service 테스트")
    public void getAllHotelDataServiceTest() throws Exception {
        //given
        Pageable pageable = createPageable();

        List<Hotel> hotels = Utils.createHotels(user, 10);
        Page<Hotel> page = new PageImpl<>(hotels, pageable, 10);

        lenient().when(hotelDao.findAll(any(Pageable.class))).thenReturn(page);

        //when
        HotelService hotelService = new HotelService(hotelDao, facilityDao, userDao, hotelMapper);
        Slice<HotelResponse> sliceData = hotelService.getAllHotelData(pageable);

        //then
        Assertions.assertThat(sliceData.getContent().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("검색 숙소 조회 service 테스트")
    public void getSearchHotelDataServiceTest() throws Exception {
        //given
        Pageable pageable = createPageable();

        List<Hotel> hotels = Utils.createHotels(user, 10);
        Slice<HotelResponse> pageHotelData = new SliceImpl<>(hotels, pageable, true).map(hotelMapper::changeToHotelResponse);

        lenient().when(hotelDao.search(hotelSearchRequest, pageable)).thenReturn(pageHotelData);

        //when
        HotelService hotelService = new HotelService(hotelDao, facilityDao, userDao, hotelMapper);
        Slice<HotelResponse> sliceData = hotelService.searchHotelData(hotelSearchRequest, pageable);

        //then
        Assertions.assertThat(sliceData.getContent().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("특정 숙소 조회 service 테스트")
    public void getHotelDataByIdServiceTest() {
        //given
        Optional<Hotel> optionalHotel = Optional.ofNullable(hotel);
        lenient().when(hotelDao.findById(any())).thenReturn(optionalHotel);

        //when
        HotelService hotelService = new HotelService(hotelDao, facilityDao, userDao, hotelMapper);
        Optional<HotelResponse> hotelResponse = hotelService.getHotelDataById(1L);

        //then
        Assertions.assertThat(hotelResponse).isNotNull();
    }

    @Test
    @DisplayName("숙소 저장 service 테스트")
    public void saveHotelDataServiceTest() {
        //given
        Optional<User> optionalUser = Optional.of(user);

        lenient().when(hotelDao.save(any(Hotel.class))).thenReturn(hotel);
        lenient().when(userDao.findById(any())).thenReturn(optionalUser);
        lenient().when(facilityDao.save(any(Facility.class))).thenReturn(facility);
        lenient().when(hotelMapper.changeToHotel(hotelCreateRequest, user)).thenReturn(hotel);
        lenient().when(hotelMapper.changeToFacility(hotelCreateRequest, hotel)).thenReturn(facility);

        //when
        HotelService hotelService = new HotelService(hotelDao, facilityDao, userDao, hotelMapper);
        Long hotelId = hotelService.saveHotelData(hotelCreateRequest, 1L);

        //then
        Assertions.assertThat(hotelId).isEqualTo(hotel.getId());
    }

    @Test
    @DisplayName("숙소 수정 service 테스트")
    public void updateHotelDataServiceTest() {
        //given
        Optional<Hotel> optionalHotel = Optional.of(hotel);
        Optional<Facility> optionalFacility = Optional.of(facility);

        lenient().when(hotelDao.findById(any())).thenReturn(optionalHotel);
        lenient().when(facilityDao.findByHotelId(any())).thenReturn(optionalFacility);
        lenient().when(hotelDao.save(any(Hotel.class))).thenReturn(hotel);
        lenient().when(facilityDao.save(any(Facility.class))).thenReturn(facility);

        //when
        HotelService hotelService = new HotelService(hotelDao, facilityDao, userDao, hotelMapper);
        hotelService.updateHotelData(hotelUpdateRequest, hotel.getId());

        //then
        verify(hotelMapper, times(1)).updateHotelFromDto(hotelUpdateRequest, hotel);
        verify(hotelMapper, times(1)).updateFacilityFromDto(hotelUpdateRequest, facility);
    }

    @Test
    @DisplayName("숙소 삭제 service 테스트")
    public void deleteHotelDataServiceTest() {
        //given
        Optional<Hotel> optionalHotel = Optional.of(hotel);
        lenient().when(hotelDao.findById(any())).thenReturn(optionalHotel);

        //when
        HotelService hotelService = new HotelService(hotelDao, facilityDao, userDao, hotelMapper);
        hotelService.deleteHotelData(hotel.getId());

        //then
        verify(hotelDao, times(1)).delete(hotel);
    }

    private Pageable createPageable() {
        return PageRequest.of(0, 10);
    }

    private HotelSearchRequest createSearchRequest() {
        return HotelSearchRequest.builder()
            .name("업체명")
            .category(HotelCategoryEnum.HOTEL)
            .province("강원도")
            .district("양양군")
            .detailedAddress("양양로")
            .parkingLot(true)
            .wifi(true)
            .barbecue(false)
            .build();
    }
    private HotelCreateRequest createCreateRequest(Hotel hotel) {
        return HotelCreateRequest.builder()
            .userId(hotel.getUser().getId())
            .name(hotel.getName())
            .category(hotel.getCategory())
            .description(hotel.getDescription())
            .notice(hotel.getNotice())
            .province(hotel.getProvince())
            .city(hotel.getCity())
            .district(hotel.getDistrict())
            .detailedAddress(hotel.getDetailedAddress())
            .latitude(hotel.getLatitude())
            .longtitude(hotel.getLongtitude())
            .mainImage(hotel.getMainImage())
            .businessNumber(hotel.getBusinessNumber())
            .certUrl(hotel.getCertUrl())
            .visitGuidance(hotel.getVisitGuidance())
            .parkingLot(hotel.getFacility().getParkingLot())
            .wifi(hotel.getFacility().getWifi())
            .barbecue(hotel.getFacility().getBarbecue())
            .build();
    }
    private HotelUpdateRequest createUpdateRequest(Hotel hotel) {
        return HotelUpdateRequest.builder()
            .userId(hotel.getUser().getId())
            .name(hotel.getName())
            .category(hotel.getCategory())
            .description(hotel.getDescription())
            .notice(hotel.getNotice())
            .province(hotel.getProvince())
            .city(hotel.getCity())
            .district(hotel.getDistrict())
            .detailedAddress(hotel.getDetailedAddress())
            .latitude(hotel.getLatitude())
            .longtitude(hotel.getLongtitude())
            .mainImage(hotel.getMainImage())
            .businessNumber(hotel.getBusinessNumber())
            .certUrl(hotel.getCertUrl())
            .visitGuidance(hotel.getVisitGuidance())
            .parkingLot(hotel.getFacility().getParkingLot())
            .wifi(hotel.getFacility().getWifi())
            .barbecue(hotel.getFacility().getBarbecue())
            .build();
    }
}
