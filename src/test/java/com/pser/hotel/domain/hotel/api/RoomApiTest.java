package com.pser.hotel.domain.hotel.api;

import static com.pser.hotel.domain.hotel.util.Utils.createUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pser.hotel.domain.hotel.application.RoomService;
import com.pser.hotel.domain.hotel.dto.request.RoomSearchRequest;
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
import org.springframework.test.web.servlet.MockMvc;
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
}