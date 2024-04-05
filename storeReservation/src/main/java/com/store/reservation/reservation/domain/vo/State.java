package com.store.reservation.reservation.domain.vo;

import com.store.reservation.reservation.constants.state.ArrivalState;
import com.store.reservation.reservation.constants.state.ReservationState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State {
    @Builder.Default
    private ReservationState reservationState = ReservationState.READY;
    @Builder.Default
    private ArrivalState arrivalState = ArrivalState.READY;

    public void changeArrivalState(ArrivalState arrivalState) {
        this.arrivalState = arrivalState;
    }

    public void changeReservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
    }

}
