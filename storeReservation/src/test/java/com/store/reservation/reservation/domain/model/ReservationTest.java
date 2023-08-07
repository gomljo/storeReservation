package com.store.reservation.reservation.domain.model;

import com.store.reservation.member.domain.Member;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.model.type.ApprovalState;
import com.store.reservation.reservation.reservation.domain.model.type.ArrivalState;
import com.store.reservation.reservation.reservation.domain.model.type.ReservationState;
import com.store.reservation.reservation.reservation.domain.vo.Time;
import com.store.reservation.reservation.reservation.domain.vo.State;
import com.store.reservation.store.store.domain.model.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {


    @Test
    @DisplayName("예약 객체 생성 시 예약 상태 디폴트 값 검증")
    void success_createReservationWithDefaultState(){

        // given

        Member customer = Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("customer@email.com")
                .password("asdfasdfadsf")
                .build();
        Member manager = Member.builder()
                .memberId(12L)
                .name("꼬꼬닭")
                .email("manager@email.com")
                .password("asdfasdfadsf")
                .build();
        Store store = Store.builder()
                .storeId(1L)
                .manager(manager)
                .build();
        // when
        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .time(new Time(LocalDateTime.now()))
                .customer(customer)
                .store(store)
                .state(new State())
                .build();
        State state = reservation.getState();
        // then

        assertEquals(state.getReservationState(), ReservationState.READY);
        assertEquals(state.getApprovalState(), ApprovalState.READY);
        assertEquals(state.getArrivalState(), ArrivalState.READY);
    }

}