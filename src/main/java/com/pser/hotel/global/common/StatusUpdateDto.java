package com.pser.hotel.global.common;

import com.pser.hotel.domain.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDto<T extends StatusEnum> {
    private Long id;

    private String merchantUid;

    private T targetStatus;
}