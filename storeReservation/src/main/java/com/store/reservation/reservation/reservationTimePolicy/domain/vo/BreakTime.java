package com.store.reservation.reservation.reservationTimePolicy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BreakTime {

    private LocalTime startTime;
    private LocalTime endTime;
}
