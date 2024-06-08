package com.pser.hotel.domain.hotel.api;

import static com.pser.hotel.domain.hotel.util.Utils.createImageUrl;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pser.hotel.domain.hotel.application.RoomService;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import com.pser.hotel.domain.hotel.dto.request.RoomSearchRequest;
import com.pser.hotel.domain.member.domain.User;
import java.time.LocalTime;
import java.util.List;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayName("객실 Api 테스트")
class RoomApiTest {
    @InjectMocks
    RoomApi roomApi;
    @Mock
    RoomService roomService;
    MockMvc mockMvc;
    RoomSearchRequest roomSearchRequest;
    User user;
    PageRequest pageRequest;

    @BeforeEach
    public void setUp() {
        pageRequest = PageRequest.of(0, 10);
        user = createUser();
        roomSearchRequest = null;
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roomApi)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("객실 리스트 조회 테스트")
    public void roomList() throws Exception {
        long hotelId = 1;
        mockMvc.perform(get(String.format("/hotels/%d/rooms", hotelId))
                        .param("page", String.valueOf(pageRequest.getOffset()))
                        .param("size", String.valueOf(pageRequest.getPageSize())))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("단건 객실 조회 테스트")
    public void roomDetails() throws Exception {
        long hotelId = 1;
        long roomId = 1;
        String url = String.format("/hotels/%d/rooms/%d", hotelId, roomId);
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("객실 삭제 테스트")
    public void roomDelete() throws Exception {
        // Given
        Long hotelId = 1L;
        Long userId = 1L;
        String url = String.format("/hotels/%d/rooms/%d", hotelId, userId);
        System.out.println(url);
        // When
        mockMvc.perform(delete(url)
                .header("user-id", 1)).andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("객실 삭제 테스트 - 요청헤더(user-id)가 없으면 BadRequest를 응답한다.")
    public void roomDeleteByBadRequest() throws Exception {
        mockMvc.perform(delete("/hotels/1/rooms/1"))
                .andExpect(status().isBadRequest());
    }

    private RoomRequest createRoomRequestDto() {
        return new RoomRequest(
                "객실이름", "설명", "mainImage.url", "주의사항", 1000, LocalTime.of(15, 00), LocalTime.of(11, 00),
                1, 1, 1,
                true, true, true, true,
                true, true, true, true,
                true, true, true, true, true, true,
                List.of(createImageUrl(), createImageUrl())
        );
    }

    private User createUser() {
        return User.builder().email("test_email").password("123").build();
    }

    public String getRoomByJson() {
        RoomRequest dto = createRoomRequestDto();
        return String.format(
                "{\"name\" : \"%s\", \"description\" : \"%s\","
                        + " \"precaution\" : \"%s\", \"price\" : \"%d\", \"standardCapacity\" : \"%d\","
                        + " \"maxCapacity\" : \"%d\", \"totalRooms\" : \"%d\", \"heatingSystem\" : \"%s\","
                        + " \"tv\" : \"%s\", \"refrigerator\" : \"%s\", \"airConditioner\" : \"%s\","
                        + " \"washer\" : \"%s\", \"terrace\" : \"%s\", \"coffeeMachine\" : \"%s\","
                        + " \"internet\" : \"%s\", \"kitchen\" : \"%s\", \"bathtub\" : \"%s\","
                        + " \"iron\" : \"%s\", \"pool\" : \"%s\", \"pet\" : \"%s\","
                        + " \"inAnnex\" : \"%s\"}"
                , dto.getName(), dto.getDescription(), dto.getPrecaution(),
                dto.getPrice(), dto.getStandardCapacity(), dto.getMaxCapacity(), dto.getTotalRooms(),
                dto.getHeatingSystem(), dto.getTv(), dto.getRefrigerator(), dto.getAirConditioner(),
                dto.getWasher(), dto.getTerrace(),
                dto.getCoffeeMachine(), dto.getInternet(), dto.getKitchen(), dto.getBathtub(), dto.getIron(),
                dto.getPool(), dto.getPet(), dto.getInAnnex());
    }
}