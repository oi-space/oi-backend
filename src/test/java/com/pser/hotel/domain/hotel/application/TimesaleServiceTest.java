package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.dao.TimesaleDao;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import com.pser.hotel.domain.hotel.dto.TimesaleCreateRequest;
import com.pser.hotel.domain.hotel.dto.TimesaleHotelResponse;
import com.pser.hotel.domain.hotel.dto.TimesaleMapper;
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
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public class TimesaleServiceTest {
    @Mock
    TimesaleDao timesaleDao;
    @Mock
    RoomDao roomDao;
    @Mock
    HotelDao hotelDao;
    @Mock
    TimesaleMapper timesaleMapper;
    MockMvc mockMvc;
    User user;
    Hotel hotel;
    Room room;
    TimeSale timesale;
    PageRequest pageRequest;
    TimesaleCreateRequest timesaleCreateRequest;

    @BeforeEach
    public void setUp() {
        pageRequest = PageRequest.of(0, 10);
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        room = Utils.createRoom(hotel);
        timesale = Utils.createTimesale(room);
        timesaleCreateRequest = createTimesaleCreateRequest(timesale);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("타임특가 등록 Service 테스트")
    public void saveTimesaleDataTest() {
        //given
        Optional<Room> optionalRoom = Optional.of(room);

        BDDMockito.lenient().when(roomDao.findById(BDDMockito.any())).thenReturn(optionalRoom);
        BDDMockito.lenient().when(timesaleMapper.changeToTimesale(timesaleCreateRequest, room)).thenReturn(timesale);
        BDDMockito.lenient().when(timesaleDao.save(timesale)).thenReturn(timesale);

        //when
        TimesaleService timesaleService = new TimesaleService(timesaleMapper, timesaleDao, roomDao, hotelDao);
        Long timesaleId = timesaleService.saveTimesaleData(timesaleCreateRequest);

        //then
        Assertions.assertThat(timesaleId).isEqualTo(timesale.getId());
    }

    @Test
    @DisplayName("타임특가 삭제 Service 테스트")
    public void deleteTimesaleDataTest() {
        //given
        Optional<TimeSale> optionalTimeSale = Optional.of(timesale);
        BDDMockito.lenient().when(timesaleDao.findById(BDDMockito.any())).thenReturn(optionalTimeSale);

        //when
        TimesaleService timesaleService = new TimesaleService(timesaleMapper, timesaleDao, roomDao, hotelDao);
        timesaleService.deleteTimesaleData(timesale.getId());

        //then
        BDDMockito.verify(timesaleDao, Mockito.times(1)).delete(timesale);
    }

    @Test
    @DisplayName("타임특가 숙소 전체 조회 Service 테스트")
    public void getAllTimesaleHotelDataTest() {
        //given
        Pageable pageable = createPageable();

        List<Hotel> hotels = Utils.createHotels(user, 10);
        List<TimesaleHotelResponse> list = createTimesaleHotelResponseList(hotels);
        Slice<TimesaleHotelResponse> pageTimesaleHotelData = new SliceImpl<>(list, pageable, true);

        BDDMockito.lenient().when(timesaleDao.findNowTimesaleHotel(BDDMockito.any(Pageable.class))).thenReturn(pageTimesaleHotelData);

        //when
        TimesaleService timesaleService = new TimesaleService(timesaleMapper, timesaleDao, roomDao, hotelDao);
        Slice<TimesaleHotelResponse> sliceData = timesaleService.getAllTimesaleHotelData(pageable);

        //then
        Assertions.assertThat(sliceData.getContent().size()).isEqualTo(10);
    }

    private TimesaleCreateRequest createTimesaleCreateRequest(TimeSale timesale) {
        return TimesaleCreateRequest.builder()
                .roomId(1)
                .price(timesale.getPrice())
                .startAt(timesale.getStartAt())
                .endAt(timesale.getEndAt())
                .build();
    }

    private Pageable createPageable() {
        return PageRequest.of(0, 10);
    }

    private List<TimesaleHotelResponse> createTimesaleHotelResponseList(List<Hotel> hotels) {
        List<TimesaleHotelResponse> list = new ArrayList<>();
        for(Hotel ele : hotels) {
            double average = Utils.createAverageRating();
            int salePrice = Utils.createSalePrice();
            int previousPirce = salePrice + 5000;
            TimesaleHotelResponse hotelResponse = createHotelResponse(ele, average, salePrice, previousPirce);
            list.add(hotelResponse);
        }
        return list;
    }

    private TimesaleHotelResponse createHotelResponse(Hotel hotel, double average, int salePrice, int previousPrice){
        return TimesaleHotelResponse.builder()
                .id(hotel.getId())
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
                .salePrice(salePrice)
                .previousPrice(previousPrice)
                .gradeAverage(average)
                .build();
    }
}
