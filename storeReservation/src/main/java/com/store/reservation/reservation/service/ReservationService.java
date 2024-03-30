package com.store.reservation.reservation.service;

import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.domain.model.type.ArrivalState;
import com.store.reservation.reservation.domain.model.type.ReservationState;
import com.store.reservation.reservation.exception.ReservationError;
import com.store.reservation.reservation.exception.ReservationException;
import com.store.reservation.reservation.respository.jpa.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public void create(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public void updateReservationStatus(Long reservationId, ReservationState reservationState){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()->new ReservationException(ReservationError.NO_SUCH_RESERVATION));
        reservation.updateReservationState(reservationState);
    }

    public void updateArrivalState(Long reservationId, ArrivalState arrivalState){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()->new ReservationException(ReservationError.NO_SUCH_RESERVATION));
        reservation.updateArrivalState(arrivalState);
    }

}
