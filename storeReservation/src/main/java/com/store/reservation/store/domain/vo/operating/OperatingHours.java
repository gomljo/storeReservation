package com.store.reservation.store.domain.vo.operating;

import com.store.reservation.store.dto.common.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OperatingHours {

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime openingHours;
    private LocalTime closingHours;

    @ElementCollection
    @Builder.Default
    private List<LocalTime> reservationTimes = new ArrayList<>();
    private Integer reservationTimeInterval;

    private boolean isNotBreakTime(LocalTime currentTime) {
        return startTime.isAfter(currentTime)
                || endTime.isBefore(currentTime);
    }

    private boolean isBeforeThenClosing(LocalTime currentTime) {
        return closingHours.isAfter(currentTime);
    }

    public void defineReservationTimes() {

        this.reservationTimes = new ArrayList<>();
        LocalTime current = this.openingHours;
        while (this.isBeforeThenClosing(current)) {
            if (isNotBreakTime(current)) {
                reservationTimes.add(current);
            }
            current = current.plusMinutes(this.reservationTimeInterval);
        }

    }

    public static OperatingHours createBy(TimeDto timeDto) {

        return OperatingHours.builder()
                .openingHours(timeDto.getOpeningHour())
                .closingHours(timeDto.getClosingHour())
                .startTime(timeDto.getBreakTimeStart())
                .endTime(timeDto.getBreakTimeEnd())
                .reservationTimeInterval(timeDto.getReservationTimeInterval())
                .build();
    }
}
