package com.store.reservation.reservation.log.domain;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.model.BaseEntity;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.store.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logId;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne
    @JoinColumn(name = "reservationId")
    private Reservation reservation;

    public String getCustomerName(){
        return this.member.getName();
    }

    public String getStoreName(){
        return this.reservation.getReservedStoreName();
    }

    public Store getStore(){
        return this.reservation.getStore();
    }

    public LocalDateTime getReservedTime(){
        return this.reservation.getReservationTime();
    }
}
