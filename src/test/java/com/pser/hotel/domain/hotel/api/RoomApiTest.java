package com.pser.hotel.domain.hotel.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pser.hotel.domain.hotel.dto.RoomRequestDto;
import com.pser.hotel.domain.hotel.dto.RoomSearchRequest;
import com.pser.hotel.domain.member.domain.User;
import java.security.Principal;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayName("객실 Controller 테스트")
class RoomApiTest {
    @InjectMocks
    RoomApi roomApi;
    @Mock
    Principal principal;
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
        mockMvc.perform(get("/rooms")
                        .param("page", String.valueOf(pageRequest.getOffset()))
                        .param("size", String.valueOf(pageRequest.getPageSize())))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("단건 객실 조회 테스트")
    public void roomDetails() throws Exception {
        String url = String.format("/rooms/%d", 1L);
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("객실 검색 조회 테스트")
    public void searchRoom() throws Exception {
        mockMvc.perform(get("/rooms/search")
                        .param("keyword", "testKeyword")
                        .param("tv", "true")
                        .param("page", String.valueOf(pageRequest.getOffset()))
                        .param("size", String.valueOf(pageRequest.getPageSize())))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("객실 저장 테스트")
    public void roomSave() throws Exception {
        mockMvc.perform(post("/rooms")
                        .header("user-id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRoomByJson())
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("객실 저장 테스트 - 요청헤더(user-id)가 없으면 BadRequest를 응답한다.")
    public void roomSaveByBadRequest() throws Exception {
        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRoomByJson())
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("객실 수정 테스트")
    public void roomUpdate() throws Exception {
        mockMvc.perform(patch("/rooms/1")
                        .header("user-id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRoomByJson())
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("객실 수정 테스트 - 요청헤더(user-id)가 없으면 BadRequest를 응답한다.")
    public void roomUpdateByBadRequest() throws Exception {
        mockMvc.perform(patch("/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRoomByJson())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("객실 삭제 테스트")
    public void roomDelete() throws Exception {
        mockMvc.perform(delete("/rooms/1")
                        .header("user-id", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("객실 삭제 테스트 - 요청헤더(user-id)가 없으면 BadRequest를 응답한다.")
    public void roomDeleteByBadRequest() throws Exception {
        mockMvc.perform(delete("/rooms/1"))
                .andExpect(status().isBadRequest());
    }

    private RoomRequestDto createRoomRequestDto() {
        return new RoomRequestDto(
                1L,
                "객실이름", "설명", "주의사항", 1000,
                1, 1, 1,
                true, true, true, true,
                true, true, true, true,
                true, true, true, true, true, true
        );
    }

    private User createUser() {
        return User.builder().email("test_email").password("123").build();
    }

    public String getRoomByJson() {
        RoomRequestDto dto = createRoomRequestDto();
        return String.format(
                "{\"hotelId\" : \"%d\", \"name\" : \"%s\", \"description\" : \"%s\","
                        + " \"precaution\" : \"%s\", \"price\" : \"%d\", \"standardCapacity\" : \"%d\","
                        + " \"maxCapacity\" : \"%d\", \"totalRooms\" : \"%d\", \"heatingSystem\" : \"%s\","
                        + " \"tv\" : \"%s\", \"refrigerator\" : \"%s\", \"airConditioner\" : \"%s\","
                        + " \"washer\" : \"%s\", \"terrace\" : \"%s\", \"coffeeMachine\" : \"%s\","
                        + " \"internet\" : \"%s\", \"kitchen\" : \"%s\", \"bathtub\" : \"%s\","
                        + " \"iron\" : \"%s\", \"pool\" : \"%s\", \"pet\" : \"%s\","
                        + " \"inAnnex\" : \"%s\"}"
                , dto.getHotelId(), dto.getName(), dto.getDescription(), dto.getPrecaution(),
                dto.getPrice(), dto.getStandardCapacity(), dto.getMaxCapacity(), dto.getTotalRooms(),
                dto.getHeatingSystem(), dto.getTv(), dto.getRefrigerator(), dto.getAirConditioner(),
                dto.getWasher(), dto.getTerrace(),
                dto.getCoffeeMachine(), dto.getInternet(), dto.getKitchen(), dto.getBathtub(), dto.getIron(),
                dto.getPool(), dto.getPet(), dto.getInAnnex());
    }
}