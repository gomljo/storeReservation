package com.store.reservation.reservation.service;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.reservation.constants.state.ArrivalState;
import com.store.reservation.reservation.constants.state.ReservationState;
import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.domain.model.ReservationPolicy;
import com.store.reservation.reservation.domain.vo.Time;
import com.store.reservation.reservation.dto.ReservationSearchDto;
import com.store.reservation.reservation.dto.common.ReservationTimeDto;
import com.store.reservation.reservation.exception.reservation.ReservationError;
import com.store.reservation.reservation.exception.reservation.ReservationRuntimeException;
import com.store.reservation.reservation.exception.reservationPolicy.ReservationPolicyRuntimeException;
import com.store.reservation.reservation.respository.jpa.ReservationRepository;
import com.store.reservation.reservation.respository.queryDsl.ReservationSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.store.reservation.reservation.constants.state.ReservationState.APPROVAL;
import static com.store.reservation.reservation.constants.time.TimeLimit.TEN;
import static com.store.reservation.reservation.exception.reservation.ReservationError.NO_SUCH_RESERVATION;
import static com.store.reservation.reservation.exception.reservationPolicy.ReservationPolicyError.INVALID_RESERVATION_TIME;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSearchRepository reservationSearchRepository;

    public void create(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public void updateReservationStatus(Long reservationId, ReservationState reservationState) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationRuntimeException(NO_SUCH_RESERVATION));
        ReservationPolicy reservationPolicy = new ReservationPolicy(LocalDateTime.now());
        if (!reservationPolicy.isValidReservationTime(0, reservation.getTime().getReservedDate(),
                reservation.getTime().getReservedTime())) {
            throw new ReservationPolicyRuntimeException(INVALID_RESERVATION_TIME);
        }
        reservation.updateReservationState(reservationState);
    }

    public void updateArrivalState(Long reservationId, ArrivalState arrivalState) {
        Reservation reservation = reservationRepository.findByIdAndState_ReservationState(reservationId, APPROVAL)
                .orElseThrow(() -> new ReservationRuntimeException(NO_SUCH_RESERVATION));
        ReservationPolicy reservationPolicy = new ReservationPolicy(LocalDateTime.now());
        if (reservationPolicy.isValidArrivalTime(TEN.getMinute(), reservation.getTime().getReservedDate(),
                reservation.getTime().getReservedTime())) {
            throw new ReservationPolicyRuntimeException(INVALID_RESERVATION_TIME);
        }
        reservation.updateArrivalState(arrivalState);
    }

    public Page<Reservation> searchReservationListForCustomerBy(Long customerId, ReservationSearchDto reservationSearchDto) {
        return reservationSearchRepository.findReservationListByCustomer(customerId, reservationSearchDto);
    }

    public Page<Reservation> searchReservationListForManagerBy(Long storeId, ReservationSearchDto reservationSearchDto) {
        return reservationSearchRepository.findReservationListByStore(storeId, reservationSearchDto);
    }

    public int aggregateNumberOfReservationsBy(Long storeId, ReservationTimeDto reservationTimeDto) {
        return reservationSearchRepository.aggregateNumberOfReservationsBy(storeId, reservationTimeDto);
    }

    public void checkAlreadyReserved(MemberInformation customer, ReservationTimeDto reservationTimeDto) {
        Time reservationTime = Time.builder()
                .reservedTime(reservationTimeDto.getReservationTime())
                .reservedDate(reservationTimeDto.getReservationDate())
                .build();
        if (reservationRepository.existsByCustomerAndTime(customer, reservationTime)) {
            throw new ReservationRuntimeException(ReservationError.ALREADY_RESERVED);
        }
    }

    public Page<Reservation> searchReservationListByStoreAndDateAndTime(Long storeId, ReservationTimeDto reservationTimeDto, Pageable pageable) {
        return reservationSearchRepository.findReservationListByStoreAndDateAndTime(storeId, reservationTimeDto, pageable);
    }

}
