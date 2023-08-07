package com.store.reservation.global.reservation.service;

import com.store.reservation.global.reservation.dto.*;
import com.store.reservation.member.domain.Member;
import com.store.reservation.member.service.MemberService;
import com.store.reservation.reservation.log.domain.ReservationLog;
import com.store.reservation.reservation.log.service.ReservationLogService;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.vo.Time;
import com.store.reservation.reservation.reservation.domain.vo.State;
import com.store.reservation.reservation.reservation.service.ReservationModuleService;
import com.store.reservation.store.store.domain.model.Store;
import com.store.reservation.store.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationComponentService {

    private final ReservationModuleService reservationModuleService;
    private final StoreService storeService;
    private final MemberService memberService;
    private final ReservationLogService reservationLogService;

    public ReservationDto reserve(CreateReservation.Request request) {
        log.info("[Reservation Service]: 예약 시작");
        Store store = storeService.search(request.getStoreId());
        Member customer = memberService.searchBy(request.getCustomerId());

        Reservation reservation = reservationModuleService.registerReservation(
                Reservation.builder()
                        .customer(customer)
                        .store(store)
                        .time(new Time(request.getReservationTime()))
                        .state(new State())
                        .build());

        log.info("[Reservation Service]: 예약 완료");
        return ReservationDto.builder()
                .customerName(customer.getName())
                .customerPhoneNumber(customer.getPhoneNumber())
                .reservationTime(request.getReservationTime())
                .reservationState(reservation.getReservedState())
                .storeName(store.getStoreName())
                .build();
    }

    /**
     * 예약 승인 상태를 변경하고 싶은 예약 객체의 식별자와 승인 상태를 전달받아
     * 예약 객체의 승인 상태를 변경하는 함수
     *
     * @param request: 예약 승인 상태 값
     * @return 예약 승인 상태가 변경된 예약 객체
     */

    public Reservation updateArrivalState(long reservationId, UpdateArrival.Request request) {

        Reservation reservation = reservationModuleService.updateArrivalState(reservationId, request);

        reservationLogService.createReservationLog(
                ReservationLog.builder()
                        .member(reservation.getCustomer())
                        .reservation(reservation)
                        .build()
        );
        return reservation;
    }

    public List<StoreDto> searchStoreByDistance(Pageable pageable, SearchByCondition.Request request) {
        return storeService.searchByDistance(pageable, request);
    }

    public List<StoreDto> searchStoreByAlphabetic(Pageable pageable, SearchByCondition.Request request) {
        return storeService.searchByAlphabetic(pageable, request);
    }

    public List<StoreDto> searchStoreByStarRating(Pageable pageable, SearchByCondition.Request request) {
        return storeService.searchByStarRating(pageable, request);
    }

    public List<Reservation> searchAllReservationToday(long managerId) {
        return reservationModuleService.searchByCurrentDate(managerId);
    }
}
