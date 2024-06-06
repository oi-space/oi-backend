package com.pser.hotel.domain.hotel.dto.request;

import com.pser.hotel.domain.model.GradeEnum;
import lombok.Data;
import java.util.List;

@Data
public class ReviewCreateRequest {
    private GradeEnum grade;
    private String detail;
    private Long reservationId;
    private List<String> imageUrls;
}
