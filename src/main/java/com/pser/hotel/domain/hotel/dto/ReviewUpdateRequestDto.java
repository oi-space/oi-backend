package com.pser.hotel.domain.hotel.dto;

import com.pser.hotel.domain.model.GradeEnum;
import jakarta.validation.constraints.Null;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ReviewUpdateRequestDto {

    @Null
    private Long id;

    @Null
    private LocalDateTime createdAt;

    @Null // 업데이트 시간
    private LocalDateTime updatedAt;

    private GradeEnum grade;

    @Length(min = 10, max = 500) // 내용 길이 제한
    private String detail;
}
