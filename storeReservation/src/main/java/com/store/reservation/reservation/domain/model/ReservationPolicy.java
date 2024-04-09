package com.store.reservation.reservation.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationPolicy {

    private final LocalDateTime current;

    public ReservationPolicy(LocalDateTime current) {
        this.current = current;
    }

    public boolean isValidReservationTime(int limit, LocalDate reservationDate, LocalTime reservationTime) {
        return this.current.plusMinutes(limit).isBefore(LocalDateTime.of(reservationDate, reservationTime));
    }

    public boolean isValidArrivalTime(int limit, LocalDate reservationDate, LocalTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime);
        return (reservationDateTime.plusMinutes(limit).isAfter(this.current) || reservationDateTime.plusMinutes(limit).equals(this.current))
                && (reservationDateTime.minusMinutes(limit).isBefore(this.current) || reservationDateTime.minusMinutes(limit).equals(this.current));

    }
}