package com.pser.hotel.domain.hotel.dto.request;

import com.pser.hotel.domain.hotel.domain.Room;
import com.pser.hotel.domain.member.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationCreateRequest {
    @Null
    @Schema(hidden = true)
    private Long authId;

    @Null
    @Schema(hidden = true)
    private User user;

    @NotNull
    @Min(0)
    private Long roomId;

    @Null
    @Schema(hidden = true)
    private Room room;

    @NotNull
    @Min(0)
    private Integer visitorCount;

    @NotNull
    @Min(0)
    private Integer adultCount;

    @NotNull
    @Min(0)
    private Integer childCount;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAt;
}
