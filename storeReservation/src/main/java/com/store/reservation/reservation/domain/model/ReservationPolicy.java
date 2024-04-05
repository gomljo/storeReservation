package com.store.reservation.reservation.domain.model;

import java.time.LocalTime;

public class ReservationPolicy {

    private final LocalTime currentTime;

    public ReservationPolicy(LocalTime currentTime){
        this.currentTime = currentTime;
    }

    public boolean isCurrentTimeBeforeThanReservationTime(int limit, LocalTime reservationTime) {
        return reservationTime.minusMinutes(limit).isBefore(this.currentTime);
    }

    public boolean isCurrentTimeBeforeThanArrivalTime(int limit, LocalTime arrivalTime) {
        return arrivalTime.plusMinutes(limit).isBefore(this.currentTime)
                && this.currentTime.isAfter(arrivalTime);
    }
}
