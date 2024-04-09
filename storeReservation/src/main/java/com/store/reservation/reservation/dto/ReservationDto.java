package com.store.reservation.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.store.reservation.reservation.constants.state.ArrivalState;
import com.store.reservation.reservation.constants.state.ReservationState;
import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.dto.common.ReservationStateDto;
import com.store.reservation.reservation.dto.common.ReservationTimeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"reservationTime", "reservationDate", "arrivalState", "reservationState"})
public class ReservationDto {
    private ReservationStateDto reservationStateDto;
    private ReservationTimeDto reservationTimeDto;
    private int numberOfPeople;

    public static ReservationDto from(Reservation reservation) {
        return ReservationDto.builder()
                .reservationTimeDto(new ReservationTimeDto(reservation.getTime().getReservedDate(),
                        reservation.getTime().getReservedTime()))
                .reservationStateDto(new ReservationStateDto(reservation.getState().getReservationState(),
                        reservation.getState().getArrivalState()))
                .numberOfPeople(reservation.getNumberOfPeople())
                .build();
    }
    public LocalTime getReservationTime() {
        return reservationTimeDto.getReservationTime();
    }
    public LocalDate getReservationDate() {
        return reservationTimeDto.getReservationDate();
    }
    public ArrivalState getArrivalState() {
        return reservationStateDto.getArrivalState();
    }
    public ReservationState getReservationState() {
        return reservationStateDto.getReservationState();
    }

}
