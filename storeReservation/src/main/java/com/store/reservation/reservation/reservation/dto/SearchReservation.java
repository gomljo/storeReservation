package com.store.reservation.reservation.reservation.dto;

import com.store.reservation.reservation.reservation.domain.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


public class SearchReservation {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private List<ReservationDto> reservationDtoList;

        public static SearchReservation.Response from(List<Reservation> reservationList){
            return new Response(reservationList.stream()
                    .map(ReservationDto::fromEntity)
                    .collect(Collectors.toList()));
        }
    }
}
