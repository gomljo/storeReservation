package com.store.reservation.reservation.log.repository;

import com.store.reservation.reservation.log.domain.ReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationLogRepository extends JpaRepository<ReservationLog, Long> {

    @Query("select log " +
            "from ReservationLog log " +
            "join fetch log.member " +
            "where log.member.memberId=:customerId")
    List<ReservationLog> findByCustomerId(@Param("customerId") long customerId);
    Optional<ReservationLog> findByLogId(long reservationLogId);

}

