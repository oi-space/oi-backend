package com.pser.hotel.domain.hotel.dto.request;

import com.pser.hotel.domain.model.GradeEnum;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class ReviewUpdateRequest {

    private GradeEnum grade;

    @Size(min = 10, max = 500, message = "리뷰 내용은 최소 10자 이상, 최대 500자 이하로 작성해주세요.")
    private String detail;

    private List<String> imageUrls;

    private Long roomId;

    private String roomName;

}
