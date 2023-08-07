package com.store.reservation.global.reservation.service;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.service.MemberService;
import com.store.reservation.reservation.log.service.ReservationLogService;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.vo.Time;
import com.store.reservation.reservation.reservation.domain.vo.State;
import com.store.reservation.reservation.reservation.service.ReservationModuleService;
import com.store.reservation.store.store.domain.model.Store;
import com.store.reservation.store.store.service.StoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReservationComponentServiceTest {

    @Mock
    private ReservationModuleService reservationModuleService;

    @InjectMocks
    private ReservationComponentService reservationComponentService;


    @Test
    @DisplayName("매장의 오늘의 모든 예약 일정 조회 성공")
    void success_SearchAllReservationToday(){

        List<Reservation> reservationList = new ArrayList<>();
        Store store = Store.builder()
                .storeId(1L)
                .build();
        Member member = Member.builder()
                .memberId(1L)
                .email("xxx@email.com")
                .password("12343254")
                .phoneNumber("010-111-1111")
                .build();
        reservationList.add(Reservation.builder()
                        .reservationId(1L)
                        .state(new State())
                        .time(new Time(LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(9,0,0))))
                        .store(store)
                        .customer(member)
                .build());
        reservationList.add(Reservation.builder()
                .reservationId(2L)
                .state(new State())
                .time(new Time(LocalDateTime.of(
                        LocalDate.now(),
                        LocalTime.of(9,30,0))))
                .store(store)
                .customer(member)
                .build());


        // given
        given(reservationModuleService.searchByCurrentDate(anyLong()))
                .willReturn(reservationList);
        // when

        List<Reservation> reservations = reservationComponentService.searchAllReservationToday(1L);

        // then
        assertEquals(reservations.get(0).getReservationId(), reservationList.get(0).getReservationId());

    }
}