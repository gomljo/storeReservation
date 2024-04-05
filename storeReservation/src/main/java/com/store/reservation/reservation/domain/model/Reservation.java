package com.store.reservation.reservation.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.model.BaseEntity;
import com.store.reservation.reservation.constants.state.ArrivalState;
import com.store.reservation.reservation.constants.state.ReservationState;
import com.store.reservation.reservation.domain.vo.State;
import com.store.reservation.reservation.domain.vo.Time;
import com.store.reservation.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private long id;

    @JsonBackReference
    @ManyToOne
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
    private int numberOfPeople;

    public void updateReservationState(ReservationState reservationState) {
        this.state.changeReservationState(reservationState);
    }

    public void updateArrivalState(ArrivalState updatedArrivalState) {
        this.state.changeArrivalState(updatedArrivalState);
    }
}
