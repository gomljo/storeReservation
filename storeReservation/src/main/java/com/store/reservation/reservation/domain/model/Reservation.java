package com.store.reservation.reservation.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.model.BaseEntity;
import com.store.reservation.reservation.domain.model.type.ApprovalState;
import com.store.reservation.reservation.domain.model.type.ArrivalState;
import com.store.reservation.reservation.domain.vo.State;
import com.store.reservation.reservation.domain.vo.Time;
import com.store.reservation.reservation.exception.ReservationError;
import com.store.reservation.reservation.exception.ReservationException;
import com.store.reservation.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private long reservationId;

    @OneToOne
    @JoinColumn(name = "member_information_id")
    private MemberInformation customer;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Embedded
    private State state;

    @Embedded
    private Time time;

    public LocalDateTime getReservationTime() {
        return time.getTime();
    }

    public String getReservedStoreName() {
        return store.getStoreName();
    }

    public String getReservedState() {
        return state.getReservationState().toString();
    }

    public void updateApprovalState(ApprovalState updatedApprovalState) {
        if (!this.state.isChangedApprovalState(updatedApprovalState)) {
            throw new ReservationException(ReservationError.APPROVAL_STATE_NOT_CHANGED);
        }
        this.state.changeApprovalState(updatedApprovalState);
    }

    public void updateArrivalState(ArrivalState updatedArrivalState) {
        if (!this.state.isArrived(updatedArrivalState)) {
            throw new ReservationException(ReservationError.INVALID_ARRIVAL_STATE_UPDATE);
        }
        this.state.changeArrivalState(updatedArrivalState);
        this.state.changeReservationStateToReserved();
    }
}
