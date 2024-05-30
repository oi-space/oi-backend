package com.pser.hotel.domain.hotel.api;

import com.pser.hotel.domain.hotel.application.TimesaleService;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.domain.TimeSale;
import com.pser.hotel.domain.hotel.dto.TimesaleCreateRequest;
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
public class TimesaleApiTest {
    @InjectMocks
    TimesaleApi timesaleApi;
    @Mock
    TimesaleService timesaleService;
    MockMvc mockMvc;
    PageRequest pageRequest;
    User user;
    Hotel hotel;
    Room room;
    TimeSale timesale;

    private TimesaleCreateRequest timesaleCreateRequest;

    @BeforeEach
    public void setUp() {
        pageRequest = PageRequest.of(0, 10);
        user = Utils.createUser();
        hotel = Utils.createHotel(user);
        room = Utils.createRoom(hotel);
        timesale = Utils.createTimesale(room);
        timesaleCreateRequest = createTimesaleCreateRequest(timesale);
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(timesaleApi)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("타임특가 등록 테스트")
    public void saveTimesale() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/timesales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getCreateTimesaleByJson())
                .header("user-id", "1"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
    }

    @Test
    @DisplayName("타임특가 적용 숙소 전체 조회 테스트")
    public void getAllTimesaleHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/timesales")
                .param("page", String.valueOf(pageRequest.getOffset()))
                .param("size", String.valueOf(pageRequest.getPageSize())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("타임특가 삭제 테스트")
    public void deleteTimesale() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/timesales/1")
                .header("user-id", 1))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private TimesaleCreateRequest createTimesaleCreateRequest(TimeSale timesale) {
        return TimesaleCreateRequest.builder()
                .roomId(1)
                .price(timesale.getPrice())
                .startAt(timesale.getStartAt())
                .endAt(timesale.getEndAt())
                .build();
    }

    private String getCreateTimesaleByJson() {
        TimesaleCreateRequest dto = timesaleCreateRequest;
        return String.format(
                "{\"roomId\": \"%d\", \"price\": \"%d\", \"startAt\": \"%s\", \"endAt\": \"%s\"}",
                dto.getRoomId(), dto.getPrice(), dto.getStartAt().toString(), dto.getEndAt().toString()
        );
    }
}
