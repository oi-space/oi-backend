package com.pser.hotel.domain.hotel.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmenityDto {
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long roomId;

    private Boolean heatingSystem;

    private Boolean tv;

    private Boolean refrigerator;

    private Boolean airConditioner;

    private Boolean washer;

    private Boolean terrace;

    private Boolean coffeeMachine;

    private Boolean internet;

    private Boolean kitchen;

    private Boolean bathtub;

    private Boolean iron;

    private Boolean pool;

    private Boolean pet;

    private Boolean inAnnex;
}
