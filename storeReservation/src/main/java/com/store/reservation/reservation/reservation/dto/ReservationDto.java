package com.store.reservation.reservation.reservation.dto;

import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.model.type.ApprovalState;
import com.store.reservation.reservation.reservation.domain.model.type.ArrivalState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDto {

    private long reservationId;
    private ApprovalState approvalState;
    private ArrivalState arrivalState;
    private LocalDateTime reservedTime;

    public static ReservationDto fromEntity(Reservation reservation){
        return ReservationDto.builder()
                .reservationId(reservation.getReservationId())
                .approvalState(reservation.getState().getApprovalState())
                .arrivalState(reservation.getState().getArrivalState())
                .reservedTime(reservation.getReservationTime())
                .build();
    }

}
