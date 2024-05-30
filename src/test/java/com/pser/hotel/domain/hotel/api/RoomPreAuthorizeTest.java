package com.pser.hotel.domain.hotel.api;

import static com.pser.hotel.domain.hotel.util.Utils.createHotel;
import static com.pser.hotel.domain.hotel.util.Utils.createRoom;
import static com.pser.hotel.domain.hotel.util.Utils.createUser;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.request.RoomRequest;
import com.pser.hotel.domain.member.domain.User;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DisplayName("객실 Api 테스트")
@Slf4j
public class RoomPreAuthorizeTest {
    @Autowired
    MockMvc mockMvc;
    @SpyBean
    HotelDao hotelDao;
    @SpyBean
    RoomDao roomDao;
    User user;
    Hotel hotel;
    Room room;
    RoomRequest roomRequest;
    Long userId = 1L;
    Random rnd = new Random();

    @BeforeEach
    public void setUp() {
        roomRequest = createRoomRequestDto();
    }

    @Test
    @DisplayName("객실 저장 테스트")
    public void roomSave() throws Exception {
        // Given
        user = Mockito.spy(createUser());
        hotel = Mockito.spy(createHotel(user));

        given(hotelDao.findByIdAndUserId(roomRequest.getHotelId(), userId)).willReturn(Optional.ofNullable(hotel));
        given(hotelDao.findById(roomRequest.getHotelId())).willReturn(Optional.ofNullable(hotel));

        // When
        ResultActions resultActions = mockMvc.perform(post("/rooms")
                .header("user-id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRoomByJson())
        );

        // Then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("객실 저장 테스트 - 자신의 호텔 정보가 없으면 예외를 발생시킨다.")
    public void roomSaveByForbiddenException() throws Exception {
        // Given

        // When
        ResultActions resultActions = mockMvc.perform(post("/rooms")
                .header("user-id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRoomByJson())
        );

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("객실 수정 테스트")
    public void roomUpdate() throws Exception {
        // Given
        Long roomId = 1L;
        user = Mockito.spy(createUser());
        hotel = Mockito.spy(createHotel(user));
        room = Mockito.spy(createRoom(hotel));

        given(hotelDao.findByIdAndUserId(roomRequest.getHotelId(), userId)).willReturn(Optional.ofNullable(hotel));
        given(hotelDao.findById(roomRequest.getHotelId())).willReturn(Optional.ofNullable(hotel));
        given(roomDao.findByIdAndHotelId(roomId, hotel.getId())).willReturn(Optional.ofNullable(room));

        // When
        ResultActions resultActions = mockMvc.perform(patch(String.format("/rooms/%d", roomId))
                .header("user-id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRoomByJson())
        );

        // Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("객실 수정 테스트 - 자신의 호텔 정보가 없으면 예외를 발생시킨다.")
    public void roomUpdateByForbiddenException() throws Exception {
        // Given
        Long roomId = 1L;

        // When
        ResultActions resultActions = mockMvc.perform(patch(String.format("/rooms/%d", roomId))
                .header("user-id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getRoomByJson())
        );

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("객실 삭제 테스트")
    public void roomDelete() throws Exception {
        // Given
        Long roomId = 1L;
        user = Mockito.spy(createUser());
        hotel = Mockito.spy(createHotel(user));
        room = Mockito.spy(createRoom(hotel));

        given(room.getId()).willReturn(roomId);
        given(hotelDao.findByIdAndUserId(roomRequest.getHotelId(), userId)).willReturn(Optional.ofNullable(hotel));
        given(hotelDao.findById(roomRequest.getHotelId())).willReturn(Optional.ofNullable(hotel));
        given(roomDao.findByIdAndHotelId(roomId, hotel.getId())).willReturn(Optional.ofNullable(room));

        // When
        ResultActions resultActions = mockMvc.perform(
                delete(String.format("/hotels/%d/rooms/%d", roomRequest.getHotelId(), roomId))
                        .header("user-id", userId));

        // Then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("객실 삭제 테스트 - 자신의 호텔 정보가 없으면 예외를 발생시킨다.")
    public void roomDeleteByForbiddenException() throws Exception {
        // Given
        Long roomId = 1L;

        // When
        ResultActions resultActions = mockMvc.perform(
                delete(String.format("/hotels/%d/rooms/%d", roomRequest.getHotelId(), roomId))
                        .header("user-id", userId));

        // Then
        resultActions.andExpect(status().isForbidden());
    }


    private RoomRequest createRoomRequestDto() {
        return new RoomRequest(
                1L,
                "객실이름", "설명", "주의사항", 1000, LocalTime.of(15, 00), LocalTime.of(11, 00),
                1, 1, 1,
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), List.of("test.jpg")
        );
    }

    public String getRoomByJson() {
        RoomRequest dto = createRoomRequestDto();

        return String.format(
                "{\"hotelId\" : \"%d\", \"name\" : \"%s\", \"description\" : \"%s\","
                        + " \"precaution\" : \"%s\", \"price\" : \"%d\", \"checkIn\" : \"%s\",\"checkOut\" : \"%s\",\"standardCapacity\" : \"%d\","
                        + " \"maxCapacity\" : \"%d\", \"totalRooms\" : \"%d\", \"heatingSystem\" : \"%s\","
                        + " \"tv\" : \"%s\", \"refrigerator\" : \"%s\", \"airConditioner\" : \"%s\","
                        + " \"washer\" : \"%s\", \"terrace\" : \"%s\", \"coffeeMachine\" : \"%s\","
                        + " \"internet\" : \"%s\", \"kitchen\" : \"%s\", \"bathtub\" : \"%s\","
                        + " \"iron\" : \"%s\", \"pool\" : \"%s\", \"pet\" : \"%s\","
                        + " \"inAnnex\" : \"%s\", \"imgUrls\" : [\"%s\"]}"
                , dto.getHotelId(), dto.getName(), dto.getDescription(), dto.getPrecaution(),
                dto.getPrice(), dto.getCheckIn(), dto.getCheckOut(), dto.getStandardCapacity(), dto.getMaxCapacity(),
                dto.getTotalRooms(),
                dto.getHeatingSystem(), dto.getTv(), dto.getRefrigerator(), dto.getAirConditioner(),
                dto.getWasher(), dto.getTerrace(),
                dto.getCoffeeMachine(), dto.getInternet(), dto.getKitchen(), dto.getBathtub(), dto.getIron(),
                dto.getPool(), dto.getPet(), dto.getInAnnex(),
                "http://image.com");
    }
}
