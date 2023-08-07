package com.store.reservation.reservation.log.dto;

import com.store.reservation.reservation.log.domain.ReservationLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationLogDto {

    private long logId;
    private String storeName;
    private LocalDateTime reservationTime;
    private String customerName;

    public static ReservationLogDto fromEntity(ReservationLog reservationLog){
        return ReservationLogDto.builder()
                .customerName(reservationLog.getCustomerName())
                .logId(reservationLog.getLogId())
                .reservationTime(reservationLog.getReservedTime())
                .storeName(reservationLog.getStoreName())
                .build();
    }
}
