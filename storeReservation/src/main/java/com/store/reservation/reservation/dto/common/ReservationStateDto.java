package com.store.reservation.reservation.dto.common;

import com.store.reservation.reservation.constants.state.ArrivalState;
import com.store.reservation.reservation.constants.state.ReservationState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReservationStateDto {
    private ReservationState reservationState;
    private ArrivalState arrivalState;
}
