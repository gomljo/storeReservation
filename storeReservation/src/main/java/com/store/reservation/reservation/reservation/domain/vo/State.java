package com.store.reservation.reservation.reservation.domain.vo;

import com.store.reservation.reservation.reservation.domain.model.type.ApprovalState;
import com.store.reservation.reservation.reservation.domain.model.type.ArrivalState;
import com.store.reservation.reservation.reservation.domain.model.type.ReservationState;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class State {

    private ReservationState reservationState;

    private ApprovalState approvalState;

    private ArrivalState arrivalState;

    public State() {
        this.reservationState = ReservationState.READY;
        this.approvalState = ApprovalState.READY;
        this.arrivalState = ArrivalState.READY;
    }

    public boolean isChangedApprovalState(ApprovalState approvalState) {

        return this.approvalState != approvalState;
    }

    public void changeApprovalState(ApprovalState approvalState) {
        this.approvalState = approvalState;
    }

    public boolean isArrived(ArrivalState arrivalState) {
        return arrivalState == ArrivalState.ARRIVED;
    }

    public void changeArrivalState(ArrivalState arrivalState) {
        this.arrivalState = arrivalState;
    }

    public void changeReservationStateToReserved() {
        this.reservationState = ReservationState.RESERVED;
    }

}
