package com.store.reservation.reservation.reservation.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Time {

    private LocalDate reservedDate;
    private LocalTime reservedTime;

    public Time(LocalDateTime reservedTime){
        this.reservedDate = parseDateFrom(reservedTime);
        this.reservedTime =  parseTimeFrom(reservedTime);
    }


    private LocalDate parseDateFrom(LocalDateTime reservedTime){
        return LocalDate.of(
                reservedTime.getYear(),
                reservedTime.getMonth(),
                reservedTime.getDayOfMonth());
    }

    private LocalTime parseTimeFrom(LocalDateTime reservedTime){
        return LocalTime.of(
                reservedTime.getHour(),
                reservedTime.getMinute(),
                reservedTime.getSecond());
    }
    public LocalDateTime getTime(){
        return LocalDateTime.of(this.reservedDate, this.reservedTime);
    }


}
