package com.store.reservation.reservation.reservation.service;

import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.exception.ReservationException;
import com.store.reservation.reservation.respository.ReservationRepository;
import com.store.reservation.store.repository.jpa.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.store.reservation.reservation.exception.ReservationError.ALREADY_RESERVED;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationModuleService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    public Reservation registerReservation(Reservation reservation) {
        if (reservationRepository.existsReservationByTime(reservation.getTime())) {
            throw new ReservationException(ALREADY_RESERVED);
        }
        return reservationRepository.save(reservation);
    }

}
