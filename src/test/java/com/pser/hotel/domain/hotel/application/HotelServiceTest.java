package com.pser.hotel.domain.hotel.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.pser.hotel.domain.hotel.dao.FacilityDao;
import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.HotelImageDao;
import com.pser.hotel.domain.hotel.dao.UserDao;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.HotelCategoryEnum;
import com.pser.hotel.domain.hotel.dto.mapper.HotelMapper;
import com.pser.hotel.domain.hotel.dto.request.HotelCreateRequest;
import com.pser.hotel.domain.hotel.dto.request.HotelSearchRequest;
import com.pser.hotel.domain.hotel.dto.request.HotelUpdateRequest;
import com.pser.hotel.domain.hotel.dto.response.HotelResponse;
import com.pser.hotel.domain.hotel.dto.response.HotelSummaryResponse;
import com.pser.hotel.domain.hotel.kafka.producer.HotelStatusProducer;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @Mock
    HotelStatusProducer hotelStatusProducer;

    @Mock
    HotelDao hotelDao;

    @Mock
    FacilityDao facilityDao;

    @Mock
    UserDao userDao;

    @Mock
    HotelMapper hotelMapper;

    @Mock
    HotelImageDao hotelImageDao;

    @InjectMocks
    HotelService hotelService;

    MockMvc mockMvc;
    User user;
    Hotel hotel;
    Facility facility;
    PageRequest pageRequest;
    HotelSearchRequest hotelSearchRequest;
    HotelCreateRequest hotelCreateRequest;
    HotelUpdateRequest hotelUpdateRequest;
    List<String> hotelImages;

    @BeforeEach
    public void setUp() {
        pageRequest = PageRequest.of(0, 10);
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        facility = Utils.createFacility(hotel);
        hotelImages = Utils.createHotelImages();
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
        List<HotelSummaryResponse> list = createHotelSummaryResponseList(hotels);
        Slice<HotelSummaryResponse> pageHotelData = new SliceImpl<>(list, pageable, true);

        lenient().when(hotelDao.findAllWithGradeAndPrice(any(Pageable.class))).thenReturn(pageHotelData);

        //when
        Slice<HotelSummaryResponse> sliceData = hotelService.getAllHotelData(pageable);

        //then
        Assertions.assertThat(sliceData.getContent().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("검색 숙소 조회 service 테스트")
    public void getSearchHotelDataServiceTest() throws Exception {
        //given
        Pageable pageable = createPageable();

        List<Hotel> hotels = Utils.createHotels(user, 10);
        List<HotelSummaryResponse> list = createHotelSummaryResponseList(hotels);
        Slice<HotelSummaryResponse> pageHotelData = new SliceImpl<>(list, pageable, true);

        lenient().when(hotelDao.search(hotelSearchRequest, pageable)).thenReturn(pageHotelData);

        //when
        Slice<HotelSummaryResponse> sliceData = hotelService.searchHotelData(hotelSearchRequest, pageable);

        //then
        Assertions.assertThat(sliceData.getContent().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("특정 숙소 조회 service 테스트")
    public void getHotelDataByIdServiceTest() {
        //given
        double average = Utils.createAverageRating();
        int salePrice = Utils.createSalePrice();
        int previousPirce = salePrice + 5000;
        HotelResponse response = createHotelResponse(hotel, average, salePrice, previousPirce);
        lenient().when(hotelDao.findHotel(any())).thenReturn(response);

        //when
        HotelResponse hotelResponse = hotelService.getHotelDataById(1L);

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
                .breakfast(true)
                .build();
    }

    private HotelCreateRequest createCreateRequest(Hotel hotel) {
        return HotelCreateRequest.builder()
                .name(hotel.getName())
                .category(hotel.getCategory())
                .description(hotel.getDescription())
                .notice(hotel.getNotice())
                .province(hotel.getProvince())
                .city(hotel.getCity())
                .district(hotel.getDistrict())
                .detailedAddress(hotel.getDetailedAddress())
                .latitude(hotel.getLatitude())
                .longitude(hotel.getLongitude())
                .mainImage(hotel.getMainImage())
                .businessNumber(hotel.getBusinessNumber())
                .certUrl(hotel.getCertUrl())
                .visitGuidance(hotel.getVisitGuidance())
                .parkingLot(hotel.getFacility().getParkingLot())
                .wifi(hotel.getFacility().getWifi())
                .barbecue(hotel.getFacility().getBarbecue())
                .sauna(hotel.getFacility().getSauna())
                .swimmingPool(hotel.getFacility().getSwimmingPool())
                .restaurant(hotel.getFacility().getRestaurant())
                .roofTop(hotel.getFacility().getRoofTop())
                .fitness(hotel.getFacility().getFitness())
                .dryer(hotel.getFacility().getDryer())
                .breakfast(hotel.getFacility().getBreakfast())
                .smokingArea(hotel.getFacility().getSmokingArea())
                .allTimeDesk(hotel.getFacility().getAllTimeDesk())
                .luggageStorage(hotel.getFacility().getLuggageStorage())
                .snackBar(hotel.getFacility().getSnackBar())
                .petFriendly(hotel.getFacility().getPetFriendly())
                .hotelImageUrls(hotelImages)
                .build();
    }

    private HotelUpdateRequest createUpdateRequest(Hotel hotel) {
        return HotelUpdateRequest.builder()
                .name(hotel.getName())
                .category(hotel.getCategory())
                .description(hotel.getDescription())
                .notice(hotel.getNotice())
                .province(hotel.getProvince())
                .city(hotel.getCity())
                .district(hotel.getDistrict())
                .detailedAddress(hotel.getDetailedAddress())
                .latitude(hotel.getLatitude())
                .longitude(hotel.getLongitude())
                .mainImage(hotel.getMainImage())
                .businessNumber(hotel.getBusinessNumber())
                .certUrl(hotel.getCertUrl())
                .visitGuidance(hotel.getVisitGuidance())
                .parkingLot(hotel.getFacility().getParkingLot())
                .wifi(hotel.getFacility().getWifi())
                .barbecue(hotel.getFacility().getBarbecue())
                .sauna(hotel.getFacility().getSauna())
                .swimmingPool(hotel.getFacility().getSwimmingPool())
                .restaurant(hotel.getFacility().getRestaurant())
                .roofTop(hotel.getFacility().getRoofTop())
                .fitness(hotel.getFacility().getFitness())
                .dryer(hotel.getFacility().getDryer())
                .breakfast(hotel.getFacility().getBreakfast())
                .smokingArea(hotel.getFacility().getSmokingArea())
                .allTimeDesk(hotel.getFacility().getAllTimeDesk())
                .luggageStorage(hotel.getFacility().getLuggageStorage())
                .snackBar(hotel.getFacility().getSnackBar())
                .petFriendly(hotel.getFacility().getPetFriendly())
                .hotelImageUrls(hotelImages)
                .build();
    }

    private HotelResponse createHotelResponse(Hotel hotel, double average, int salePrice, int previousPrice) {
        return HotelResponse.builder()
                .id(hotel.getId())
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
                .longitude(hotel.getLongitude())
                .mainImage(hotel.getMainImage())
                .businessNumber(hotel.getBusinessNumber())
                .certUrl(hotel.getCertUrl())
                .visitGuidance(hotel.getVisitGuidance())
                .parkingLot(hotel.getFacility().getParkingLot())
                .wifi(hotel.getFacility().getWifi())
                .barbecue(hotel.getFacility().getBarbecue())
                .sauna(hotel.getFacility().getSauna())
                .swimmingPool(hotel.getFacility().getSwimmingPool())
                .restaurant(hotel.getFacility().getRestaurant())
                .roofTop(hotel.getFacility().getRoofTop())
                .fitness(hotel.getFacility().getFitness())
                .dryer(hotel.getFacility().getDryer())
                .breakfast(hotel.getFacility().getBreakfast())
                .smokingArea(hotel.getFacility().getSmokingArea())
                .allTimeDesk(hotel.getFacility().getAllTimeDesk())
                .luggageStorage(hotel.getFacility().getLuggageStorage())
                .snackBar(hotel.getFacility().getSnackBar())
                .petFriendly(hotel.getFacility().getPetFriendly())
                .gradeAverage(average)
                .salePrice(salePrice)
                .previousPrice(previousPrice)
                .build();
    }

    private List<HotelSummaryResponse> createHotelSummaryResponseList(List<Hotel> hotels) {
        List<HotelSummaryResponse> list = new ArrayList<>();
        for (Hotel ele : hotels) {
            double average = Utils.createAverageRating();
            int salePrice = Utils.createSalePrice();
            int previousPirce = salePrice + 5000;
            HotelSummaryResponse hotelSummaryResponse = createHotelSummaryResponse(ele, average, salePrice,
                    previousPirce);
            list.add(hotelSummaryResponse);
        }
        return list;
    }

    private HotelSummaryResponse createHotelSummaryResponse(Hotel ele, double average, int salePrice,
                                                            int previousPrice) {
        return HotelSummaryResponse.builder()
                .id(ele.getId())
                .name(ele.getName())
                .category(ele.getCategory())
                .description(ele.getDescription())
                .mainImage(ele.getMainImage())
                .gradeAverage(average)
                .salePrice(salePrice)
                .previousPrice(previousPrice)
                .build();
    }
}
