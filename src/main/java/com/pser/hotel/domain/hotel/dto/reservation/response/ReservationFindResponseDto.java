package com.pser.hotel.domain.hotel.dto.reservation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationFindResponseDto {
    // pagenation을 위한 리턴값들

    // 총 페이지 수
    private Integer totalPages;

    // 현제 페이지
    private Integer currentPage;

    // 총 내용 수
    private Long totalElements;

    private List<ReservationFindDetailResponseDto> reservationFindDetailResponseDto;
}
