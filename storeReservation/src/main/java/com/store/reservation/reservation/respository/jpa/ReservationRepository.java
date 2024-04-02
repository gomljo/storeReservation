package com.store.reservation.reservation.respository.jpa;


import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.reservation.constants.state.ReservationState;
import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.domain.vo.Time;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    boolean existsByCustomerAndTime(MemberInformation customer, Time time);

    Optional<Reservation> findByIdAndState_ReservationState(Long id, ReservationState reservationState);


}
