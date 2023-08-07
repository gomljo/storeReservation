package com.store.reservation.reservation.reservation.respository;


import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.vo.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(Long reservationId);

    @Query("select reservation " +
            "from Reservation reservation " +
            "join fetch reservation.store " +
            "where reservation.time.reservedDate = :today and reservation.store.storeId = :storeId " +
            "order by reservation.time.reservedTime ASC "
    )
    List<Reservation> findAllByCurrentDateAndStore(
            @Param("today") LocalDate today,
            @Param("storeId") long storeId);

    boolean existsReservationByTime(Time time);
}
