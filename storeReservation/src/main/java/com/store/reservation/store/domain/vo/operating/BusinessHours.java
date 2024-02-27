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
public class BusinessHours {
    private LocalTime openingHours;
    private LocalTime closingHours;
    public boolean isBeforeThenClosing(LocalTime currentTime) {
        return closingHours.isAfter(currentTime);
    }
}
