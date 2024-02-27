package com.store.reservation.store.domain.vo.operating;

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
    public boolean isNotBreakTime(LocalTime currentTime) {
        return startTime.isAfter(currentTime)
                || endTime.isBefore(currentTime);
    }
}
