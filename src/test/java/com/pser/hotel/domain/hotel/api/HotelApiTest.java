package com.pser.hotel.domain.hotel.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pser.hotel.domain.hotel.dto.HotelCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class HotelApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private HotelCreateRequest hotelCreateRequest;

    PageRequest pageRequest;

    @BeforeEach
    public void setUp(){
        pageRequest = PageRequest.of(0, 10);
        hotelCreateRequest = createHotelCreateRequest();
    }

    @Test
    @DisplayName("숙소 등록 테스트")
    public void saveHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hotelCreateRequest))
                .header("user-id", "12345"))
            .andExpect(MockMvcResultMatchers.status().isCreated());
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
            .content(objectMapper.writeValueAsString(hotelCreateRequest)))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("특정 숙소 삭제 테스트")
    public void deleteHotel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/hotels/1")
            .header("user-id", 1))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private HotelCreateRequest createHotelCreateRequest() {
        return new HotelCreateRequest(
            12345L,
            "Test Hotel",
            "Test Category",
            "Test Description",
            "Test Notice",
            "Test Province",
            "Test City",
            "Test District",
            "Test Detailed Address",
            37.1234,
            127.1234,
            "Test Main Image",
            "Test Business Number",
            "Test Cert URL",
            "Test Visit Guidance",
            true,
            true,
            false
        );
    }
}
