package com.store.reservation.reservation.respository;


import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.domain.vo.Time;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsReservationByTime(Time time);
}
