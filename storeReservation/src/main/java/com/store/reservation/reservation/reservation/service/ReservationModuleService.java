package com.store.reservation.reservation.reservation.service;

import com.store.reservation.global.reservation.dto.UpdateArrival;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.model.type.ApprovalState;
import com.store.reservation.reservation.reservation.dto.UpdateApproval;
import com.store.reservation.reservation.reservation.exception.ReservationException;
import com.store.reservation.reservation.reservation.respository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.store.reservation.reservation.reservation.exception.ReservationError.ALREADY_RESERVED;
import static com.store.reservation.reservation.reservation.exception.ReservationError.NO_SUCH_RESERVATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationModuleService {

    private final ReservationRepository reservationRepository;

    public Reservation registerReservation(Reservation reservation) {
        log.info("예약 등록 시작");
        if(reservationRepository.existsReservationByTime(reservation.getTime())){
            log.error("이미 예약 등록된 시간입니다.");
            throw new ReservationException(ALREADY_RESERVED);
        }
        log.info("예약 등록 완료");
        return reservationRepository.save(reservation);
    }

    public Reservation updateApprovalState(long reservationId, ApprovalState approvalState) {
        log.info("예약 승인 여부 변경 시작");
        Reservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(
                        () -> new ReservationException(NO_SUCH_RESERVATION));

        reservation.updateApprovalState(approvalState);
        log.info("예약 승인 여부 변경 정상 종료");
        return reservationRepository.save(reservation);
    }

    public Reservation updateArrivalState(long reservationId, UpdateArrival.Request request) {
        log.info("예약자 도착 여부 변경 시작");
        Reservation reservation = reservationRepository
                .findById(reservationId)
                .orElseThrow(
                        () -> new ReservationException(NO_SUCH_RESERVATION));

        reservation.updateArrivalState(request.getArrivalState());
        log.info("예약자 도착 여부 변경 정상 종료");
        return reservationRepository.save(reservation);
    }

    public List<Reservation> searchByCurrentDate(long managerId) {
        log.info("당일 예약 요청 목록 조회");
        return reservationRepository.findAllByCurrentDateAndStore(LocalDate.now(), managerId);
    }


}
