package com.store.reservation.reservation.reservation.dto;

import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.model.type.ApprovalState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UpdateApproval {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        ApprovalState approvalState;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private long reservationId;
        private LocalDateTime reservationTime;
        private String storeName;
        private ApprovalState approvalState;

        public static UpdateApproval.Response fromEntity(Reservation reservation) {
            return Response.builder()
                    .reservationId(reservation.getReservationId())
                    .storeName(reservation.getStore().getStoreName())
                    .reservationTime(reservation.getReservationTime())
                    .approvalState(reservation.getState().getApprovalState())
                    .build();
        }
    }
}
