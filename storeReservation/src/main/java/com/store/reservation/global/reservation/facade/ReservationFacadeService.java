package com.store.reservation.global.reservation.facade;

import com.store.reservation.aop.lock.annotation.DistributedLock;
import com.store.reservation.common.dto.SearchResponse;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.model.SecurityUser;
import com.store.reservation.member.service.MemberService;
import com.store.reservation.reservation.constants.state.ArrivalState;
import com.store.reservation.reservation.constants.state.ReservationState;
import com.store.reservation.reservation.constants.time.TimeLimit;
import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.domain.model.ReservationPolicy;
import com.store.reservation.reservation.domain.vo.State;
import com.store.reservation.reservation.domain.vo.Time;
import com.store.reservation.reservation.dto.ReservationDetailForCustomerDto;
import com.store.reservation.reservation.dto.ReservationDto;
import com.store.reservation.reservation.dto.ReservationSearchDto;
import com.store.reservation.reservation.dto.common.ReservationTimeDto;
import com.store.reservation.reservation.exception.reservation.ReservationRuntimeException;
import com.store.reservation.reservation.exception.reservationPolicy.ReservationPolicyRuntimeException;
import com.store.reservation.reservation.service.ReservationService;
import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.store.reservation.reservation.exception.reservation.ReservationError.*;
import static com.store.reservation.reservation.exception.reservationPolicy.ReservationPolicyError.INVALID_RESERVATION_TIME;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationFacadeService {
    private final ReservationService reservationService;
    private final StoreService storeService;
    private final MemberService memberService;

    @DistributedLock(key = "#storeId", group = "reservation")
    public void reserve(Long storeId, Long customerId, ReservationDto reservationDto) {
        MemberInformation customer = memberService.searchBy(customerId);
        Store store = storeService.searchStoreByCustomer(storeId);
        ReservationPolicy reservationPolicy = new ReservationPolicy(LocalDateTime.now());

        if (!store.isCorrectReservationTime(reservationDto.getReservationTime())) {
            throw new ReservationRuntimeException(ILLEGAL_RESERVATION_TIME);
        }
        if (!reservationPolicy.isValidReservationTime(TimeLimit.TEN.getMinute(),
                reservationDto.getReservationDate(),
                reservationDto.getReservationTime())) {
            throw new ReservationPolicyRuntimeException(INVALID_RESERVATION_TIME);
        }
        reservationService.checkAlreadyReserved(customer, reservationDto.getReservationTimeDto());

        int numberOfReservations = reservationService.aggregateNumberOfReservationsBy(storeId,
                reservationDto.getReservationTimeDto());

        if (!store.isPossibleToReserve(numberOfReservations)) {
            throw new ReservationRuntimeException(NOT_AVAILABLE);
        }

        reservationService.create(Reservation.builder()
                .customer(customer)
                .numberOfPeople(reservationDto.getNumberOfPeople())
                .time(Time.builder()
                        .reservedDate(reservationDto.getReservationDate())
                        .reservedTime(reservationDto.getReservationTime())
                        .build())
                .state(State.builder()
                        .arrivalState(reservationDto.getArrivalState())
                        .reservationState(reservationDto.getReservationState())
                        .build())
                .store(store)
                .build());
    }

    @DistributedLock(key = "#storeId", group = "reservation")
    public void updateReservationState(Long managerId, Long reservationId, Long storeId, ReservationState reservationState) {
        MemberInformation manager = memberService.searchBy(managerId);
        Store store = storeService.searchStoreByOwner(manager, storeId);


        if (!store.getMemberInformation().isSame(managerId)) {
            throw new ReservationRuntimeException(ILLEGAL_ACCESS);
        }

        reservationService.updateReservationStatus(reservationId, reservationState);
    }

    @DistributedLock(key = "#storeId", group = "reservation")
    public void updateArrivalState(Long managerId, Long reservationId, Long storeId, ArrivalState arrivalState) {
        MemberInformation manager = memberService.searchBy(managerId);
        Store store = storeService.searchStoreByOwner(manager, storeId);
        if (!store.getMemberInformation().isSame(managerId)) {
            throw new ReservationRuntimeException(ILLEGAL_ACCESS);
        }
        reservationService.updateArrivalState(reservationId, arrivalState);
    }

    public SearchResponse<ReservationDetailForCustomerDto> searchReservationListForCustomer(SecurityUser customerAuth, Long customerId,
                                                                                            ReservationSearchDto reservationSearchDto) {
        MemberInformation customer = memberService.searchBy(customerAuth.getId());
        if(!customer.isSame(customerId)){
            throw new ReservationRuntimeException(ILLEGAL_ACCESS);
        }
        return SearchResponse.from(reservationService.searchReservationListForCustomerBy(customerId, reservationSearchDto),
                ReservationDetailForCustomerDto::from);
    }

    public SearchResponse<ReservationDto> searchReservationListForManager(Long managerId, Long storeId,
                                                                          ReservationSearchDto reservationSearchDto) {

        MemberInformation manager = memberService.searchBy(managerId);
        Store store = storeService.searchStoreByOwner(manager, storeId);
        if (!store.getMemberInformation().isSame(managerId)) {
            throw new ReservationRuntimeException(ILLEGAL_ACCESS);
        }

        return SearchResponse.from(reservationService.searchReservationListForManagerBy(storeId, reservationSearchDto),
                ReservationDto::from);
    }

    public SearchResponse<ReservationDto> searchReservationListByDateAndTimeForManager(Long managerId, Long storeId,
                                                                                       ReservationTimeDto reservationTimeDto,
                                                                                       Pageable pageable) {
        MemberInformation manager = memberService.searchBy(managerId);

        Store store = storeService.searchStoreByOwner(manager, storeId);

        if (!store.getMemberInformation().isSame(managerId)) {
            throw new ReservationRuntimeException(ILLEGAL_ACCESS);
        }

        if (!store.isCorrectReservationTime(reservationTimeDto.getReservationTime())) {
            throw new ReservationRuntimeException(ILLEGAL_RESERVATION_TIME);
        }

        return SearchResponse.from(reservationService.searchReservationListByStoreAndDateAndTime(storeId, reservationTimeDto, pageable), ReservationDto::from);

    }
}
