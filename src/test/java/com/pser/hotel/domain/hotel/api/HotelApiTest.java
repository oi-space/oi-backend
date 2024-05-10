package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.HotelService;
import com.pser.hotel.domain.hotel.domain.Facility;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.dto.HotelCreateRequest;
import com.pser.hotel.domain.hotel.dto.HotelUpdateRequest;
import com.pser.hotel.domain.hotel.util.Utils;
import com.pser.hotel.domain.member.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class HotelApiTest {
    @InjectMocks
    HotelApi hotelApi;
    @Mock
    HotelService hotelService;
    MockMvc mockMvc;
    private HotelCreateRequest hotelCreateRequest;
    private HotelUpdateRequest hotelUpdateRequest;
    User user;
    Hotel hotel;
    Facility facility;
    PageRequest pageRequest;

    @BeforeEach
    public void setUp(){
        pageRequest = PageRequest.of(0, 10);
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        facility = Utils.createFacility(hotel);
        hotelCreateRequest = createHotelCreateRequest(hotel);
        hotelUpdateRequest = updateHotelCreateRequest(hotel);
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hotelApi)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    @DisplayName("숙소 등록 테스트")
    public void saveHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/hotels")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getCreateHotelByJson())
            .header("user-id", "1"))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();
    }

    @Test
    @DisplayName("숙소 전체 조회 테스트")
    public void getAllHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels")
            .param("page", String.valueOf(pageRequest.getOffset()))
            .param("size", String.valueOf(pageRequest.getPageSize())))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("숙소 검색 조회 테스트")
    public void searchHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/search")
            .param("page", String.valueOf(pageRequest.getOffset()))
            .param("size", String.valueOf(pageRequest.getPageSize()))
            .param("barbecue", "true")
            .param("wifi", "false"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("특정 숙소 조회 테스트")
    public void getHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hotels/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("특정 숙소 수정 테스트")
    public void updateHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/hotels/1")
            .header("user-id", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(getUpdateHotelByJson()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("특정 숙소 삭제 테스트")
    public void deleteHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/hotels/1")
            .header("user-id", 1))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private HotelCreateRequest createHotelCreateRequest(Hotel hotel) {
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
            .build();
    }

    private HotelUpdateRequest updateHotelCreateRequest(Hotel hotel) {
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
            .build();
    }

    private String getCreateHotelByJson() {
        HotelCreateRequest dto = hotelCreateRequest;
        return String.format(
            "{\"name\": \"%s\", \"category\": \"%s\", \"description\": \"%s\", \"notice\": \"%s\", \"province\": \"%s\", " +
                "\"city\": \"%s\", \"district\": \"%s\", \"detailedAddress\": \"%s\", \"latitude\": \"%f\", \"longtitude\": \"%f\", " +
                "\"mainImage\": \"%s\", \"businessNumber\": \"%s\", \"certUrl\": \"%s\", \"visitGuidance\": \"%s\", " +
                "\"parkingLot\": \"%b\", \"wifi\": \"%b\", \"barbecue\": \"%b\", \"sauna\": \"%b\", \"swimmingPool\": \"%b\", " +
                "\"restaurant\": \"%b\", \"roofTop\": \"%b\", \"fitness\": \"%b\", \"dryer\": \"%b\", \"breakfast\": \"%b\", " +
                "\"smokingArea\": \"%b\", \"allTimeDesk\": \"%b\", \"luggageStorage\": \"%b\", \"snackBar\": \"%b\", " +
                "\"petFriendly\": \"%b\"}"
            , dto.getName(), dto.getCategory(), dto.getDescription(), dto.getNotice(), dto.getProvince()
            , dto.getCity(), dto.getDistrict(), dto.getDetailedAddress(), dto.getLatitude(), dto.getLongtitude(), dto.getMainImage()
            , dto.getBusinessNumber(), dto.getCertUrl(), dto.getVisitGuidance(), dto.getParkingLot(), dto.getWifi(), dto.getBarbecue()
            , dto.getSauna(), dto.getSwimmingPool(), dto.getRestaurant(), dto.getRoofTop(), dto.getFitness(), dto.getDryer()
            , dto.getBreakfast(), dto.getSmokingArea(), dto.getAllTimeDesk(), dto.getLuggageStorage(), dto.getSnackBar()
            , dto.getPetFriendly()
        );
    }
    private String getUpdateHotelByJson() {
        HotelUpdateRequest dto = hotelUpdateRequest;
        return String.format(
            "{\"name\": \"%s\", \"category\": \"%s\", \"description\": \"%s\", \"notice\": \"%s\", \"province\": \"%s\", " +
                "\"city\": \"%s\", \"district\": \"%s\", \"detailedAddress\": \"%s\", \"latitude\": \"%f\", \"longtitude\": \"%f\", " +
                "\"mainImage\": \"%s\", \"businessNumber\": \"%s\", \"certUrl\": \"%s\", \"visitGuidance\": \"%s\", " +
                "\"parkingLot\": \"%b\", \"wifi\": \"%b\", \"barbecue\": \"%b\", \"sauna\": \"%b\", \"swimmingPool\": \"%b\", " +
                "\"restaurant\": \"%b\", \"roofTop\": \"%b\", \"fitness\": \"%b\", \"dryer\": \"%b\", \"breakfast\": \"%b\", " +
                "\"smokingArea\": \"%b\", \"allTimeDesk\": \"%b\", \"luggageStorage\": \"%b\", \"snackBar\": \"%b\", " +
                "\"petFriendly\": \"%b\"}"
            , dto.getName(), dto.getCategory(), dto.getDescription(), dto.getNotice(), dto.getProvince()
            , dto.getCity(), dto.getDistrict(), dto.getDetailedAddress(), dto.getLatitude(), dto.getLongtitude(), dto.getMainImage()
            , dto.getBusinessNumber(), dto.getCertUrl(), dto.getVisitGuidance(), dto.getParkingLot(), dto.getWifi(), dto.getBarbecue()
            , dto.getSauna(), dto.getSwimmingPool(), dto.getRestaurant(), dto.getRoofTop(), dto.getFitness(), dto.getDryer()
            , dto.getBreakfast(), dto.getSmokingArea(), dto.getAllTimeDesk(), dto.getLuggageStorage(), dto.getSnackBar()
            , dto.getPetFriendly()
        );
    }
}
