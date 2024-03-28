package com.store.reservation.reservation.dto;


import com.store.reservation.reservation.domain.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;


public class SearchReservation {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private List<ReservationDto> reservationDtoList;

        public static SearchReservation.Response from(Page<Reservation> reservationList) {
            return new Response(reservationList.getContent().stream()
                    .map(ReservationDto::fromEntity)
                    .collect(Collectors.toList()));
        }
    }
}
