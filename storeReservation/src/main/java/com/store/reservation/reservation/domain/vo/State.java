package com.store.reservation.reservation.domain.vo;

import com.store.reservation.reservation.constants.state.ArrivalState;
import com.store.reservation.reservation.constants.state.ReservationState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State {

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState = ReservationState.READY;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ArrivalState arrivalState = ArrivalState.READY;

    public void changeArrivalState(ArrivalState arrivalState) {
        this.arrivalState = arrivalState;
    }

    public void changeReservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
    }

}
