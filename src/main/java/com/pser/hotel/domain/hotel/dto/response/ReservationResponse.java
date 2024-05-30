package com.pser.hotel.domain.hotel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pser.hotel.domain.hotel.domain.ReservationStatusEnum;
import jakarta.persistence.Column;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponse {
    private Long userId;

    private Long roomId;

    private String merchantUid;

    private String impUid;

    private Integer price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endAt;

    private Integer visitorCount;

    private Integer adultCount;

    private Integer childCount;

    private ReservationStatusEnum status;
}
