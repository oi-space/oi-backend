package com.pser.hotel.domain.hotel.application;

import static com.pser.hotel.domain.hotel.util.Utils.createHotel;
import static com.pser.hotel.domain.hotel.util.Utils.createRoom;
import static com.pser.hotel.domain.hotel.util.Utils.createRooms;
import static com.pser.hotel.domain.hotel.util.Utils.createUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.pser.hotel.domain.hotel.dao.HotelDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.domain.Hotel;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.RoomRequest;
import com.pser.hotel.domain.hotel.dto.RoomResponse;
import com.pser.hotel.domain.hotel.dto.RoomSearchRequest;
import com.pser.hotel.domain.member.domain.User;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("객실 Service 테스트")
@Slf4j
class RoomServiceTest {
    Random rnd = new Random();
    @InjectMocks
    RoomService roomService;
    @Mock
    RoomDao roomDao;
    @Mock
    HotelDao hotelDao;
    User user;
    Hotel hotel;
    Room room;
    RoomRequest requestDto;

    @BeforeEach
    public void setUp() {
        user = Mockito.spy(createUser());
        hotel = Mockito.spy(createHotel(user));
        room = Mockito.spy(createRoom(hotel));
    }

    @Test
    @DisplayName("save 테스트")
    public void saveTest() {
        // Given
        long userId = 1;
        requestDto = createRoomRequestDto();
        given(hotelDao.findByIdAndUserId(userId, requestDto.getHotelId())).willReturn(Optional.of(hotel));

        // When
        Long save = roomService.save(userId, requestDto);

        // Then
        then(roomDao).should().save(any());
    }

    @Test
    @DisplayName("findRoomList 테스트")
    public void findRoomList() {
        // Given
        List<Room> roomList = createRooms(hotel, 10);
        Pageable pageable = createPageable(0, 10);
        Page<Room> roomPage = new PageImpl<>(roomList, pageable, 10);

        given(roomDao.findAll(pageable)).willReturn(roomPage);

        // When
        Page<RoomResponse> result = roomService.findRoomList(pageable);

        // Then
        Assertions.assertThat(result).isEqualTo(roomPage.map(e -> e.toDto()));
    }

    @Test
    @DisplayName("findRoom 테스트")
    public void findRoomTest() {
        // Given
        given(roomDao.findById(1L)).willReturn(Optional.of(room));

        // When
        RoomResponse result = roomService.findRoom(1L);

        //Then
        Assertions.assertThat(result).isEqualTo(room.toDto());
    }

    @Test
    @DisplayName("search 테스트")
    public void searchTest() {
        // Given
        RoomSearchRequest searchRequest = createSearchReqeust();
        List<Room> roomList = createRooms(hotel, 10);
        Pageable pageable = createPageable(0, 10);
        Page<RoomResponse> expect = new PageImpl<>(roomList, pageable, 10).map(e -> e.toDto());

        given(roomDao.search(searchRequest, pageable)).willReturn(expect);

        // When
        Page<RoomResponse> actual = roomService.search(searchRequest, pageable);

        // Then
        Assertions.assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("update 테스트")
    public void updateTest() {
        // Given
        Long roomId = 1L;
        Long userId = 1L;
        requestDto = createRoomRequestDto();
        given(hotel.getId()).willReturn(1L);
        given(hotelDao.findByIdAndUserId(requestDto.getHotelId(), userId)).willReturn(Optional.of(hotel));
        given(roomDao.findByIdAndHotelId(roomId, requestDto.getHotelId())).willReturn(Optional.of(room));

        // When
        roomService.update(userId, roomId, requestDto);

        // Then
        then(roomDao).should().save(any());
    }

    @Test
    @DisplayName("remove 테스트")
    public void removeTest() {
        // Given
        Long userId = 1L;
        Long hotelId = 1L;
        Long roomId = 1L;
        given(hotel.getId()).willReturn(hotelId);
        given(room.getId()).willReturn(roomId);
        given(hotelDao.findByIdAndUserId(hotelId, userId)).willReturn(Optional.of(hotel));
        given(roomDao.findByIdAndHotelId(roomId, hotelId)).willReturn(Optional.of(room));

        // When
        roomService.remove(userId, hotelId, roomId);

        // Then
        then(roomDao).should().deleteById(roomId);
    }

    private RoomSearchRequest createSearchReqeust() {
        return RoomSearchRequest.builder()
                .keyword("test_keyword")
                .build();
    }

    private RoomRequest createRoomRequestDto() {
        return new RoomRequest(
                1L,
                "객실이름", "설명", "주의사항", 1000, LocalTime.of(15, 00), LocalTime.of(11, 00),
                1, 1, 1,
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(), rnd.nextBoolean(),
                rnd.nextBoolean()
        );
    }

    private Pageable createPageable(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber, pageSize);
    }
}