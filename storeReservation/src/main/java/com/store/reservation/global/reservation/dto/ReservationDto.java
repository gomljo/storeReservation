package com.store.reservation.global.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {

    private String customerName;
    private String customerPhoneNumber;
    private LocalDateTime reservationTime;
    private String storeName;
    private String reservationState;

}
