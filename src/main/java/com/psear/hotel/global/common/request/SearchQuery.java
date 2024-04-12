package com.psear.hotel.global.common.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchQuery {
    private String keyword;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private LocalDateTime updatedAfter;
    private LocalDateTime updatedBefore;
}
