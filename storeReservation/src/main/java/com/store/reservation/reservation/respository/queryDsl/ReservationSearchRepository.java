package com.store.reservation.reservation.respository.queryDsl;


import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.dto.ReservationSearchDto;
import com.store.reservation.reservation.dto.common.ReservationTimeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationSearchRepository {


    int aggregateNumberOfReservationsBy(Long storeId, ReservationTimeDto reservationTimeDto);

    Page<Reservation> findReservationListByStore(Long storeId, ReservationSearchDto reservationSearchDto);

    Page<Reservation> findReservationListByCustomer(Long customerId, ReservationSearchDto reservationSearchDto);

    Page<Reservation> findReservationListByStoreAndDateAndTime(Long storeId, ReservationTimeDto reservationTimeDto,
                                                               Pageable pageable);
}
