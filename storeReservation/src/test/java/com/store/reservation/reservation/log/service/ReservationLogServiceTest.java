package com.store.reservation.reservation.log.service;

import com.store.reservation.reservation.log.repository.ReservationLogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationLogServiceTest {

    @Mock
    private ReservationLogRepository reservationLogRepository;

    @InjectMocks
    private ReservationLogService reservationLogService;


    @Test
    @DisplayName("예약 로그 저장 성공")
    void success_createReservationLog(){

        // given
//        Reservation reservation = Reservation.builder()
//                .reservationId(1L)
//                .reservedTime(new ReservedTime(LocalDateTime.now()))
//                .state(new State())
//                .build();

        // when


        // then


    }

}