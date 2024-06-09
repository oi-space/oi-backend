package com.pser.hotel.domain.hotel.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String grade;

    private String detail;

    private Long reservationId;

    private List<String> reviewImages;

    private Long hotelId;

    private String reviewerName;

    private String profileImageUrl;

    private Long roomId;

    private String roomName;


}
