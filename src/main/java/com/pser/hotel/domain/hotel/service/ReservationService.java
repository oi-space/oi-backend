package com.pser.hotel.domain.hotel.service;

import com.pser.hotel.domain.hotel.dao.ReservationDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.ReservationEnum;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationSaveRequestDto;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationUpdateRequestDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.*;
import com.pser.hotel.domain.member.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// 모든 에러는 IllegalArgException으로 작성 하였습니다.
// 차후에 exception handler 적용하시면 됩니다.
@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationDao reservationDao;
    private final RoomDao roomDao;
    // 페이징을 위한 페이지 사이즈 - 수정 하시면 됩니다.
    // 혹은 yml에 따로 지정한 뒤 나중에 @Value 이용하셔서 관리하셔도 됩니다.
    private final Integer pageSize = 10;

    public ReservationFindResponseDto findAllByUserEmail(Integer page, String userEmail){
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Reservation> result = reservationDao.findByUserEmail(pageable, userEmail);
        List<ReservationFindDetailResponseDto> detail = new ArrayList<>();
        Long totalElements = result.getTotalElements();
        Integer totalPages = result.getTotalPages();

        for (Reservation reservation : result) {
            ReservationFindDetailResponseDto reservationFindDetailResponseDto = new ReservationFindDetailResponseDto(reservation, userEmail, reservation.getRoom().getName());
            detail.add(reservationFindDetailResponseDto);
        }

        return ReservationFindResponseDto.builder()
                .totalPages(totalPages)
                .totalElements(totalElements)
                .currentPage(page)
                .reservationFindDetailResponseDto(detail)
                .build();
    }

    public ReservationSaveResponseDto save(ReservationSaveRequestDto reservationSaveRequestDto){
        String roomName = reservationSaveRequestDto.getRoomName();
        LocalDate startAt = reservationSaveRequestDto.getStartAt();
        LocalDate endAt = reservationSaveRequestDto.getEndAt();



        if(endAt.isBefore(startAt)){
            throw new IllegalArgumentException("end at is before start at");
        }
        // 인원 계산 규칙을 몰라 일단 합연산 진행 했습니다.
        if(reservationSaveRequestDto.getReservationCapacity() < reservationSaveRequestDto.getAdultCapacity() + reservationSaveRequestDto.getChildCapacity()){
            throw new IllegalArgumentException("허용 인원 초과");
        }
        Optional<Reservation> result = reservationDao.findValidReservation(roomName, startAt, endAt);

        if(result.isPresent()){
            throw new IllegalArgumentException("reservation already exist");
        }

        else{
            Room room = roomDao.findByName(reservationSaveRequestDto.getRoomName())
                    .orElseThrow(() -> new IllegalArgumentException("room doesn't exist"));
            Reservation res = Reservation.builder()
                    // 여기에 차후 User dao 만드셔서 삽입하시면 됩니다.
                    .user(new User())
                    .room(room)
                    .price(reservationSaveRequestDto.getPrice())
                    .startAt(reservationSaveRequestDto.getStartAt())
                    .endAt(reservationSaveRequestDto.getEndAt())
                    .adultCapacity(reservationSaveRequestDto.getAdultCapacity())
                    .childCapacity(reservationSaveRequestDto.getChildCapacity())
                    .reservationCapacity(reservationSaveRequestDto.getReservationCapacity())
                    // status는 입장 전이라 이렇게 설정했습니다.
                    // 차후에 수정하시면 됩니다.
                    .status(ReservationEnum.BEFORE_ENTER_DEFAULT)
                    .build();
            reservationDao.save(res);
            return new ReservationSaveResponseDto(res, reservationSaveRequestDto.getUserEmail(), roomName);
        }

    }

    public ReservationUpdateResponseDto update(ReservationUpdateRequestDto reservationUpdateRequestDto){
        String roomName = reservationUpdateRequestDto.getRoomName();

        Reservation reservation = reservationDao.findByRoomName(roomName)
                .orElseThrow(() -> new IllegalArgumentException("reservation not found"));

        reservation.updateInformation(reservationUpdateRequestDto);

        Reservation save = reservationDao.save(reservation);

        // 차후에 user를 추가하고싶으시면 user, room dao에서 불러오시면 됩니다.
        // 여기에 getUser 쓰면 쿼리가 더 많이 날라가서 일단 삽입하지 않았습니다.
        // 서버에 부담이 안될것 같다고 사료되시면 삽입하시면 됩니다.
        return ReservationUpdateResponseDto.builder()
                .roomName(roomName)
                .childCapacity(save.getChildCapacity())
                .adultCapacity(save.getAdultCapacity())
                .startAt(save.getStartAt())
                .endAt(save.getEndAt())
                .build();
    }

    public ReservationDeleteResponseDto delete(String roomName){
        Reservation reservation = reservationDao.findByRoomName(roomName)
                .orElseThrow(() -> new IllegalArgumentException("reservation not found. room name : " + roomName));

        reservationDao.delete(reservation);

        return ReservationDeleteResponseDto.builder()
                .roomName(roomName)
                .deletedAt(LocalDateTime.now())
                .build();
    }
}
