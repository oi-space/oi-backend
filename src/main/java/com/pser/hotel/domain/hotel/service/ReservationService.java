package com.pser.hotel.domain.hotel.service;

import com.pser.hotel.domain.hotel.dao.ReservationDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.dao.UserDao;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.ReservationMapper;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationCreateRequest;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationUpdateRequestDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationDeleteResponseDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationFindDetailResponseDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationFindResponseDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationUpdateResponseDto;
import com.pser.hotel.domain.member.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 모든 에러는 IllegalArgException으로 작성 하였습니다.
// 차후에 exception handler 적용하시면 됩니다.
@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationDao reservationDao;
    private final RoomDao roomDao;
    private final UserDao userDao;
    // 페이징을 위한 페이지 사이즈 - 수정 하시면 됩니다.
    // 혹은 yml에 따로 지정한 뒤 나중에 @Value 이용하셔서 관리하셔도 됩니다.
    private final Integer pageSize = 10;
    private final ReservationMapper reservationMapper;

    public ReservationFindResponseDto findAllByUserEmail(Integer page, String userEmail) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Reservation> result = reservationDao.findByUserEmail(pageable, userEmail);
        List<ReservationFindDetailResponseDto> detail = new ArrayList<>();
        Long totalElements = result.getTotalElements();
        Integer totalPages = result.getTotalPages();

        for (Reservation reservation : result) {
            ReservationFindDetailResponseDto reservationFindDetailResponseDto = new ReservationFindDetailResponseDto(
                    reservation, userEmail, reservation.getRoom().getName());
            detail.add(reservationFindDetailResponseDto);
        }

        return ReservationFindResponseDto.builder()
                .totalPages(totalPages)
                .totalElements(totalElements)
                .currentPage(page)
                .reservationFindDetailResponseDto(detail)
                .build();
    }

    @Transactional
    public long save(ReservationCreateRequest request) {
        checkSchedule(request);

        User user = userDao.findById(request.getAuthId())
                .orElseThrow();
        Room room = roomDao.findById(request.getRoomId())
                .orElseThrow();
        request.setUser(user);
        request.setRoom(room);

        Reservation reservation = reservationMapper.toEntity(request);
        return reservationDao.save(reservation).getId();
    }

    public ReservationUpdateResponseDto update(ReservationUpdateRequestDto reservationUpdateRequestDto) {
        String roomName = reservationUpdateRequestDto.getRoomName();

        Reservation reservation = reservationDao.findByRoomName(roomName)
                .orElseThrow(() -> new IllegalArgumentException("reservation not found"));

        reservationMapper.updateReservationInfoFromRequest(reservationUpdateRequestDto, reservation);
        Reservation save = reservationDao.save(reservation);

        // 차후에 user를 추가하고싶으시면 user, room dao에서 불러오시면 됩니다.
        // 여기에 getUser 쓰면 쿼리가 더 많이 날라가서 일단 삽입하지 않았습니다.
        // 서버에 부담이 안될것 같다고 사료되시면 삽입하시면 됩니다.
        return ReservationUpdateResponseDto.builder()
                .roomName(roomName)
                .childCapacity(save.getChildCount())
                .adultCapacity(save.getAdultCount())
                .startAt(save.getStartAt())
                .endAt(save.getEndAt())
                .build();
    }

    public ReservationDeleteResponseDto delete(String roomName) {
        Reservation reservation = reservationDao.findByRoomName(roomName)
                .orElseThrow(() -> new IllegalArgumentException("reservation not found. room name : " + roomName));

        reservationDao.delete(reservation);

        return ReservationDeleteResponseDto.builder()
                .roomName(roomName)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    private void checkSchedule(ReservationCreateRequest request) {
        Room room = roomDao.findById(request.getRoomId())
                .orElseThrow();
        LocalDate startAt = request.getStartAt();

        if (startAt.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("과거 일자로 예약할 수 없습니다");
        }

        int overlappingCount = reservationDao.countOverlappingReservations(request);
        if (overlappingCount >= room.getMaxCapacity()) {
            throw new IllegalArgumentException("해당 객실은 요청 일자에 비어 있지 않습니다");
        }
    }
}
