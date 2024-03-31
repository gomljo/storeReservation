package com.store.reservation.reservation.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Time {

    private LocalDate reservedDate;
    private LocalTime reservedTime;

    public LocalDateTime getTime(){
        return LocalDateTime.of(this.reservedDate, this.reservedTime);
    }


}
