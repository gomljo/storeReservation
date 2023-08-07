package com.store.reservation.reservation.log.service;

import com.store.reservation.reservation.log.domain.ReservationLog;
import com.store.reservation.reservation.log.dto.CreateReservationLog;
import com.store.reservation.reservation.log.exception.ReservationLogErrorCode;
import com.store.reservation.reservation.log.exception.ReservationLogException;
import com.store.reservation.reservation.log.repository.ReservationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.store.reservation.reservation.log.exception.ReservationLogErrorCode.NO_SUCH_REVIEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationLogService {

    private final ReservationLogRepository reservationLogRepository;

    public void createReservationLog(ReservationLog reservationLog){
        reservationLogRepository.save(reservationLog);
    }

    public List<ReservationLog> searchAllReservationLogBy(long customerId){
        return reservationLogRepository.findByCustomerId(customerId);
    }

    public ReservationLog searchBy(long logId){
        return reservationLogRepository.findByLogId(logId)
                .orElseThrow(()->new ReservationLogException(NO_SUCH_REVIEW));
    }


}
