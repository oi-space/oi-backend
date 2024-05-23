package com.pser.hotel.domain.hotel.application;

import com.pser.hotel.domain.hotel.dao.ReservationDao;
import com.pser.hotel.domain.hotel.dao.RoomDao;
import com.pser.hotel.domain.hotel.dao.UserDao;
import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.hotel.dto.ReservationDto;
import com.pser.hotel.domain.hotel.dto.ReservationMapper;
import com.pser.hotel.domain.hotel.dto.reservation.request.ReservationCreateRequest;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationDeleteResponseDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationFindDetailResponseDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationFindResponseDto;
import com.pser.hotel.domain.hotel.dto.reservation.response.ReservationResponse;
import com.pser.hotel.domain.hotel.kafka.producer.ReservationStatusProducer;
import com.pser.hotel.domain.member.domain.User;
import com.pser.hotel.global.common.PaymentDto;
import com.pser.hotel.global.common.RefundDto;
import com.pser.hotel.global.error.ValidationFailedException;
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
    private final ReservationStatusProducer reservationStatusProducer;

    public ReservationResponse getById(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow();
        return reservationMapper.toResponse(reservation);
    }

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
        reservationDao.save(reservation);
        ReservationDto reservationDto = reservationMapper.toDto(reservation);
        reservationStatusProducer.produceCreated(reservationDto);
        return reservation.getId();
    }

    @Transactional
    public long refund(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow();
        RefundDto.builder().build();
        return 1;
    }

    @Transactional
    public ReservationStatusEnum checkPayment(long reservationId, String impUid) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow();
        ReservationStatusEnum status = reservation.getStatus();

        if (status.equals(ReservationStatusEnum.CREATED)) {
            PaymentDto paymentDto = PaymentDto.builder()
                    .impUid(impUid)
                    .amount(reservation.getPrice())
                    .merchantUid(reservation.getMerchantUid())
                    .build();
            updateToPaymentValidationRequired(paymentDto);
        }
        return status;
    }

    @Transactional
    public void updateToPaymentValidationRequired(PaymentDto paymentDto) {
        Reservation reservation = reservationDao.findByMerchantUid(paymentDto.getMerchantUid())
                .orElseThrow();
        ReservationStatusEnum status = reservation.getStatus();
        ReservationStatusEnum targetStatus = ReservationStatusEnum.PAYMENT_VALIDATION_REQUIRED;

        int paidAmount = paymentDto.getAmount();

        if (reservation.getPrice() != paidAmount) {
            throw new ValidationFailedException("결제 금액 불일치");
        }

        if (status.equals(ReservationStatusEnum.CREATED)) {
            reservation.updateImpUid(paymentDto.getImpUid());
            reservation.updateStatus(targetStatus);
            reservationStatusProducer.producePaymentValidationRequired(paymentDto);
        }
    }

    @Transactional
    public void updateToBeforeCheckin(PaymentDto paymentDto) {
        Reservation reservation = reservationDao.findByMerchantUid(paymentDto.getMerchantUid())
                .orElseThrow();
        ReservationStatusEnum status = reservation.getStatus();
        ReservationStatusEnum targetStatus = ReservationStatusEnum.BEFORE_CHECKIN;
        int paidAmount = paymentDto.getAmount();

        if (reservation.getPrice() != paidAmount) {
            throw new ValidationFailedException("결제 금액 불일치");
        }

        if (status.equals(ReservationStatusEnum.PAYMENT_VALIDATION_REQUIRED)) {
            reservation.updateStatus(targetStatus);
        }
    }

    @Transactional
    public void rollbackToCreated(String merchantUid) {
        Reservation reservation = reservationDao.findByMerchantUid(merchantUid)
                .orElseThrow();
        ReservationStatusEnum status = reservation.getStatus();
        ReservationStatusEnum targetStatus = ReservationStatusEnum.CREATED;

        if (!status.equals(targetStatus)) {
            reservation.updateStatus(targetStatus);
        }
    }

    @Transactional
    public void updateToRefundRequired(PaymentDto paymentDto) {
        Reservation reservation = reservationDao.findByMerchantUid(paymentDto.getMerchantUid())
                .orElseThrow();
        ReservationStatusEnum targetStatus = ReservationStatusEnum.REFUND_REQUIRED;

        if (!targetStatus.equals(reservation.getStatus())) {
            reservation.updateStatus(targetStatus);
            RefundDto refundDto = RefundDto.builder()
                    .impUid(paymentDto.getImpUid())
                    .merchantUid(paymentDto.getMerchantUid())
                    .build();
            reservationStatusProducer.produceRefundRequired(refundDto);
        }
    }

    @Transactional
    public void updateToAuctionOngoingStatus(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow();
        ReservationStatusEnum status = reservation.getStatus();
        ReservationStatusEnum targetStatus = ReservationStatusEnum.AUCTION_ONGOING;

        if (status.equals(targetStatus)) {
            return;
        }
        reservation.updateStatus(targetStatus);
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

    @Transactional
    public void closeReservation(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow();
        ReservationStatusEnum targetStatus = ReservationStatusEnum.PAST;
        reservation.updateStatus(targetStatus);
    }
}
