package com.pser.hotel.domain.hotel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pser.hotel.domain.model.GradeEnum;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    private GradeEnum grade;

    private String detail;

    private Long hotelId;

    private String reviewerName;

    private String profileImageUrl;

    private List<String> imageUrls;
}
