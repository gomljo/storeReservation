package com.store.reservation.global.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CreateReservation {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private long customerId;
        private long storeId;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime reservationTime;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String customerName;
        private String customerPhoneNumber;
        private LocalDateTime reservationTime;
        private String storeName;
        private String reservationState;

        public static CreateReservation.Response fromEntity(ReservationDto reservationDto) {
            return Response.builder()
                    .customerName(reservationDto.getCustomerName())
                    .customerPhoneNumber(reservationDto.getCustomerPhoneNumber())
                    .reservationTime(reservationDto.getReservationTime())
                    .storeName(reservationDto.getStoreName())
                    .reservationState(reservationDto.getReservationState())
                    .build();
        }
    }

}
