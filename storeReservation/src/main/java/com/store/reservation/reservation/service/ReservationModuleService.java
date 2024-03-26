package com.store.reservation.reservation.reservation.service;

import com.store.reservation.global.reservation.dto.UpdateArrival;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.model.type.ApprovalState;
import com.store.reservation.reservation.reservation.exception.ReservationException;
import com.store.reservation.reservation.reservation.respository.ReservationRepository;
import com.store.reservation.store.repository.jpa.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.store.reservation.reservation.reservation.exception.ReservationError.ALREADY_RESERVED;
import static com.store.reservation.reservation.reservation.exception.ReservationError.NO_SUCH_RESERVATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationModuleService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    public Reservation registerReservation(Reservation reservation) {
        if(reservationRepository.existsReservationByTime(reservation.getTime())){
            throw new ReservationException(ALREADY_RESERVED);
        }
        return reservationRepository.save(reservation);
    }

    public Reservation updateApprovalState(long reservationId, ApprovalState approvalState) {
        Reservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(
                        () -> new ReservationException(NO_SUCH_RESERVATION));

        reservation.updateApprovalState(approvalState);
        return reservationRepository.save(reservation);
    }

    public Reservation updateArrivalState(long reservationId, UpdateArrival.Request request) {
        Reservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(
                        () -> new ReservationException(NO_SUCH_RESERVATION));

        reservation.updateArrivalState(request.getArrivalState());
        return reservationRepository.save(reservation);
    }
}
