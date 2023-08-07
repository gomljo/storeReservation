package com.store.reservation.global.reservation.dto;

import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.model.type.ArrivalState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UpdateArrival {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private ArrivalState arrivalState;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private long reservationId;
        private LocalDateTime reservedTime;
        private ArrivalState arrivalState;

        public static UpdateArrival.Response fromEntity(Reservation reservation){
            return Response.builder()
                    .reservationId(reservation.getReservationId())
                    .reservedTime(reservation.getReservationTime())
                    .arrivalState(reservation.getState().getArrivalState())
                    .build();
        }
    }


}
