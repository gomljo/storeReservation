package com.store.reservation.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationStatusDto {
    private Long storeId;
    private LocalDate date;
    private int pageIndex;
    private int pageSize;
}
