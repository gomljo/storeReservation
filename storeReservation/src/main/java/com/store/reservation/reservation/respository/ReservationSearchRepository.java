package com.store.reservation.reservation.respository;


import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.domain.model.type.ReservationState;
import com.store.reservation.reservation.dto.ReservationStatusDto;
import org.springframework.data.domain.Page;

public interface ReservationSearchRepository {

    Page<Reservation> searchReservationsByDateAndStatus(ReservationState reservationStatus,
                                                        ReservationStatusDto reservationStatusDto);
    Page<Reservation> searchReservationByDate(ReservationStatusDto reservationStatusDto);
}
