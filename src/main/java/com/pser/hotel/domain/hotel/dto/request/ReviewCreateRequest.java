package com.pser.hotel.domain.hotel.dto.request;

import com.pser.hotel.domain.hotel.domain.Reservation;
import com.pser.hotel.domain.model.GradeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import java.util.List;
import lombok.Data;

@Data
public class ReviewCreateRequest {
    private GradeEnum grade;

    private String detail;

    @Null
    @Schema(hidden = true)
    private Long reservationId;

    @Null
    @Schema(hidden = true)
    private Reservation reservation;

    @Null
    @Schema(hidden = true)
    private Long hotelId;

    @Null
    @Schema(hidden = true)
    private String reviewerName;

    @Null
    @Schema(hidden = true)
    private String profileImageUrl;

    private List<String> imageUrls;

    @Null
    @Schema(hidden = true)
    private Long roomId;

    @Null
    @Schema(hidden = true)
    private String roomName;
}
