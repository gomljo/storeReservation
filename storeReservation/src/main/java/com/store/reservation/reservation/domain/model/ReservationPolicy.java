package com.store.reservation.reservation.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationPolicy {

    private final LocalTime currentTime;
    private final LocalDate currentDate;

    public ReservationPolicy(LocalDate currentDate, LocalTime currentTime) {
        this.currentTime = currentTime;
        this.currentDate = currentDate;
    }

    public boolean isValidReservationTime(int limit, LocalDate reservationDate, LocalTime reservationTime) {
        return isAfterThanCurrentDate(reservationDate)
                && isAfterThanCurrentTime(limit, reservationTime);
    }

    private boolean isAfterThanCurrentTime(int limit, LocalTime reservationTime) {
        return this.currentTime.isBefore(reservationTime.plusMinutes(limit));
    }

    private boolean isAfterThanCurrentDate(LocalDate reservationDate) {
        return this.currentDate.equals(reservationDate) || this.currentDate.isBefore(reservationDate);
    }

    private boolean isBeforeThanCurrentTime(int limit, LocalTime localTime){
        return this.currentTime.plusMinutes(limit).isAfter(localTime);
    }

    private boolean equalsThanCurrentDate(LocalDate localDate){
        return this.currentDate.equals(localDate);
    }

    public boolean isValidArrivalTime(int limit, LocalDate reservationDate, LocalTime reservationTime) {
        return (isBeforeThanCurrentTime(limit, reservationTime)
                && isAfterThanCurrentTime(limit, reservationTime))
                && equalsThanCurrentDate(reservationDate);
    }
}
